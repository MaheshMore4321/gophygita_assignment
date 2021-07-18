package sd.sym.assignment.gophygita.service.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sd.sym.assignment.gophygita.configuration.JwtUtils;
import sd.sym.assignment.gophygita.configuration.ResourceConfiguration;
import sd.sym.assignment.gophygita.constant.ERole;
import sd.sym.assignment.gophygita.dto.request.EmailRequest;
import sd.sym.assignment.gophygita.dto.request.LoginRequest;
import sd.sym.assignment.gophygita.dto.request.RegistrationRequest;
import sd.sym.assignment.gophygita.dto.response.LoginResponse;
import sd.sym.assignment.gophygita.dto.response.MessageResponse;
import sd.sym.assignment.gophygita.dto.response.UserVO;
import sd.sym.assignment.gophygita.entity.Role;
import sd.sym.assignment.gophygita.entity.User;
import sd.sym.assignment.gophygita.entity.UserEmailVerification;
import sd.sym.assignment.gophygita.repository.RoleRepository;
import sd.sym.assignment.gophygita.repository.UserEmailVerificationRepository;
import sd.sym.assignment.gophygita.repository.UserRepository;
import sd.sym.assignment.gophygita.service.email.SendMailProcess;
import sd.sym.assignment.gophygita.service.security.UserDetailsImpl;
import sd.sym.assignment.gophygita.service.security.UserDetailsServiceImpl;
import sd.sym.assignment.gophygita.utility.Utility;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static sd.sym.assignment.gophygita.constant.ApplicationConstant.FETCH_PURPOSE;
import static sd.sym.assignment.gophygita.constant.ApplicationConstant.LOGIN_PURPOSE;

@Slf4j
@Service
public class AuthenticationService {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    SendMailProcess sendMailProcess;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ResourceConfiguration resourceConfiguration;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserEmailVerificationRepository userEmailVerificationRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public Object loginUser(LoginRequest loginRequest) {

        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if(user.isActive()) {
                if(loginRequest.getUsername().equalsIgnoreCase(user.getUsername()) &&
                        encoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    log.info("Validation done");
                }
                else {
                    return new MessageResponse(false, "Invalid Username/Password");
                }
            }
            else{
                return new MessageResponse(false, "Account not Verified");
            }
        }

        try {
            LoginResponse loginResponse = new LoginResponse();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            loginResponse.setToken(jwtUtils.generateJwtToken(authentication));

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            loginResponse.setId(userDetails.getId());
            loginResponse.setUsername(userDetails.getUsername());

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> { return item.getAuthority(); })
                    .collect(Collectors.toList());
            loginResponse.setRoles(roles);
            loginResponse.setFlag(true);
            loginResponse.setMessage("login operation successfully");
            return loginResponse;
        }
        catch (Exception e) {
            log.error("login operation failed with error :: ", e);
            return new MessageResponse(false, "login operation failed");
        }
    }

    public MessageResponse registerUser(RegistrationRequest registrationRequest) {
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            return new MessageResponse(false, "Username is already taken!");
        }

        User user = new User(registrationRequest.getName(),
                registrationRequest.getUsername(),
                encoder.encode(registrationRequest.getPassword()),
                registrationRequest.getLanguage(),
                registrationRequest.getMobileNo());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        user.setRoles(roles);

        user.setActive(false);
        user = userRepository.save(user);

        String passCode = Utility.getUniqueEmailVerificationCode();
        UserEmailVerification userEmailVerification = new UserEmailVerification();
        userEmailVerification.setUserId(user.getUserId());
        userEmailVerification.setVerificationCode(passCode);
        userEmailVerification.setVerificationStatus(false);
        userEmailVerification.setPurpose(LOGIN_PURPOSE);
        userEmailVerificationRepository.save(userEmailVerification);

        //MAIL TRIGGER
        EmailRequest emailRequest = getEmailRequest(user.getLanguage(), LOGIN_PURPOSE, user.getUserId(), user.getName(), user.getUsername(), passCode);
        sendMailProcess.sendMail(emailRequest);

        return new MessageResponse(true, "User registered successfully!");
    }

    public MessageResponse mailVerificationUser(String username, String purpose, String emailPasscode) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return mailVerificationUser(user.getUserId(), purpose, emailPasscode);
        }
        return new MessageResponse(false, "Username not found.");
    }

    public MessageResponse mailVerificationUser(long userId, String purpose, String emailPasscode) {
        Optional<List<UserEmailVerification>> optionalUserEmailVerifications =
                userEmailVerificationRepository.findByUserIdAndPurposeOrderByRowIdDesc(userId, purpose);

        if(optionalUserEmailVerifications.isPresent()) {
            List<UserEmailVerification> userEmailVerificationsList = optionalUserEmailVerifications.get();
            if(userEmailVerificationsList.isEmpty()) {
                return new MessageResponse(false, "User mailVerificationUser Failed || UserId data not found");
            }
            else {
                UserEmailVerification userEmailVerification = userEmailVerificationsList.get(0);

                if(userEmailVerification.getVerificationCode().equalsIgnoreCase(emailPasscode)){
                    userEmailVerification.setVerificationStatus(true);
                    userEmailVerificationRepository.save(userEmailVerification);

                    Optional<User> userOptional = userRepository.findById(userId);
                    if(userOptional.isPresent()) {
                        User user = userOptional.get();
                        user.setActive(true);
                        user.setUpdatedTs(Utility.getNowDate());
                        userRepository.save(user);
                        return new MessageResponse(true, "User mailVerificationUser Successfully");
                    }
                    return new MessageResponse(false, "User mailVerificationUser Failed || Issue at UserId not found");
                }
                return new MessageResponse(false, "User mailVerificationUser Failed || emailPasscode not match");
            }
        }
        return new MessageResponse(false, "User mailVerificationUser Failed || Wrong UserId ");
    }

    public MessageResponse userNameExist(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            return new MessageResponse(true, "Username is exist");
        }
        return new MessageResponse(false, "Username is not exist");
    }

    public MessageResponse checkUserName(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if(user.isActive()) {
                String passCode = Utility.getUniqueEmailVerificationCode();
                UserEmailVerification userEmailVerification = new UserEmailVerification();
                userEmailVerification.setUserId(user.getUserId());
                userEmailVerification.setVerificationCode(passCode);
                userEmailVerification.setVerificationStatus(false);
                userEmailVerification.setPurpose(FETCH_PURPOSE);
                userEmailVerificationRepository.save(userEmailVerification);

                //MAIL TRIGGER
                EmailRequest emailRequest = getEmailRequest(user.getLanguage(), FETCH_PURPOSE, user.getUserId(), user.getName(), user.getUsername(), passCode);
                sendMailProcess.sendMail(emailRequest);

                return new MessageResponse(true, "Email trigger Kindly check you inbox for code");
            }
            else {
                return new MessageResponse(false, "Account not Verified");
            }
        }
        return new MessageResponse(false, "Username not found");
    }

    public EmailRequest getEmailRequest(String countryCode, String purpose, long userId, String name, String userName, String passCode) {

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(userName);

        purpose = purpose.toLowerCase();
        countryCode = countryCode.toLowerCase();
        String subject = "gophygita.mail." + purpose + "." + countryCode + ".subject";
        String body = "gophygita.mail." + purpose + "." + countryCode + ".emailBody";
        String link = "http://localhost:8080/api/auth/mailVerificationUser/" + userId + "/" + passCode;

        Properties prop = resourceConfiguration.readPropertyFile();
        emailRequest.setSubject(prop.getProperty(subject));
        emailRequest.setBody(prop.getProperty(body));
        emailRequest.parseTo(name, userName, passCode, link);

        return emailRequest;
    }

    public Object mailVerificationUserGetUserData(String username, String purpose, String passcode) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            MessageResponse messageResponse = mailVerificationUser(user.getUserId(), purpose, passcode);
            if(messageResponse.isFlag()){
                messageResponse.setObject(new UserVO(user));
            }
            return messageResponse;
        }
        return new MessageResponse(false, "Username not found.");
    }

    public MessageResponse authenticateRequest(HttpServletRequest request, HttpServletResponse response) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                if(authentication.isAuthenticated()) {
                    Optional<User> userOptional = userRepository.findByUsername(username);
                    if(userOptional.isPresent()) {
                        User user = userOptional.get();
                        messageResponse.setFlag(true);
                        messageResponse.setObject(new UserVO(user));
                        return messageResponse;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e);
        }
        messageResponse.setFlag(false);
        messageResponse.setMessage("Authorization token is not valid");
        return messageResponse;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}
