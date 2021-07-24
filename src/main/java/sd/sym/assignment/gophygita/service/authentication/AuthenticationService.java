package sd.sym.assignment.gophygita.service.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sd.sym.assignment.gophygita.configuration.ApplicationConfiguration;
import sd.sym.assignment.gophygita.configuration.ResourceConfiguration;
import sd.sym.assignment.gophygita.configuration.jwt.JwtUtils;
import sd.sym.assignment.gophygita.constant.ERole;
import sd.sym.assignment.gophygita.dao.GoPhygitalDao;
import sd.sym.assignment.gophygita.dto.request.EmailRequest;
import sd.sym.assignment.gophygita.dto.request.LoginRequest;
import sd.sym.assignment.gophygita.dto.request.RegistrationRequest;
import sd.sym.assignment.gophygita.dto.response.LoginResponse;
import sd.sym.assignment.gophygita.dto.response.MessageResponse;
import sd.sym.assignment.gophygita.dto.response.UserVO;
import sd.sym.assignment.gophygita.entity.User;
import sd.sym.assignment.gophygita.entity.UserEmailVerification;
import sd.sym.assignment.gophygita.service.GoPhygitalService;
import sd.sym.assignment.gophygita.service.SendMailProcess;
import sd.sym.assignment.gophygita.service.security.UserDetailsImpl;
import sd.sym.assignment.gophygita.utility.Utility;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static sd.sym.assignment.gophygita.constant.ApplicationConstant.FETCH_PURPOSE;
import static sd.sym.assignment.gophygita.constant.ApplicationConstant.LOGIN_PURPOSE;

@Slf4j
@Service
public class AuthenticationService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private GoPhygitalDao goPhygitalDao;

    @Autowired
    private SendMailProcess sendMailProcess;

    @Autowired
    private GoPhygitalService goPhygitalService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ResourceConfiguration resourceConfiguration;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    public MessageResponse loginUser(LoginRequest loginRequest) {

        User user = goPhygitalDao.getUserForUsername(loginRequest.getUsername());
        if(null != user) {
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
            loginResponse.setRole(roles.get(0));
            loginResponse.setFlag(true);
            loginResponse.setMessage("login operation successfully");
            return new MessageResponse(true, "login operation successfully", loginResponse);
        }
        catch (Exception e) {
            log.error("login operation failed with error :: ", e);
            return new MessageResponse(false, "login operation failed");
        }
    }

    public MessageResponse registerUser(RegistrationRequest registrationRequest) {
        if (goPhygitalDao.existsByUsername(registrationRequest.getUsername())) {
            return new MessageResponse(false, "Username is already taken!");
        }

        User user = new User(registrationRequest.getName(),
                registrationRequest.getUsername(),
                encoder.encode(registrationRequest.getPassword()),
                registrationRequest.getLanguage(),
                registrationRequest.getMobileNo());

        user.setRole(goPhygitalDao.getRole(ERole.ROLE_USER));
        user.setActive(false);
        goPhygitalDao.saveUser(user);

        String passCode = Utility.getUniqueEmailVerificationCode();
        UserEmailVerification userEmailVerification = new UserEmailVerification();
        userEmailVerification.setUserId(user.getUserId());
        userEmailVerification.setVerificationCode(passCode);
        userEmailVerification.setVerificationStatus(false);
        userEmailVerification.setPurpose(LOGIN_PURPOSE);
        goPhygitalDao.saveUserEmailVerification(userEmailVerification);

        //MAIL TRIGGER
        EmailRequest emailRequest = getEmailRequest(user.getLanguage(), LOGIN_PURPOSE, user.getUserId(), user.getName(), user.getUsername(), passCode);
        sendMailProcess.sendMail(emailRequest);

        return new MessageResponse(true, "User registered successfully!");
    }

    public MessageResponse mailVerificationUser(String username, String purpose, String emailPasscode) {
        User user = goPhygitalDao.getUserForUsername(username);
        if(null != user) {
            return mailVerificationUser(user.getUserId(), purpose, emailPasscode);
        }
        return new MessageResponse(false, "Username not found.");
    }

    public MessageResponse mailVerificationUser(long userId, String purpose, String emailPasscode) {
        List<UserEmailVerification> userEmailVerificationsList =
                goPhygitalDao.getUserEmailVerificationListOrderByRowIdDesc(userId, purpose);

        if(null == userEmailVerificationsList) {
            return new MessageResponse
                    (false, "User mailVerificationUser Failed || Wrong UserId ");
        }
        else if(userEmailVerificationsList.isEmpty()) {
            return new MessageResponse
                    (false, "User mailVerificationUser Failed || UserId data not found");
        }
        else {
            UserEmailVerification userEmailVerification = userEmailVerificationsList.get(0);

            if(userEmailVerification.getVerificationCode().equalsIgnoreCase(emailPasscode)){
                userEmailVerification.setVerificationStatus(true);
                goPhygitalDao.saveUserEmailVerification(userEmailVerification);

                User user = goPhygitalDao.getUserDataForUserId(userId);
                if(null != user) {
                    user.setActive(true);
                    user.setUpdatedTs(Utility.getNowDate());
                    goPhygitalDao.saveUser(user);
                    return new MessageResponse(
                            true, "User mailVerificationUser Successfully");
                }
                return new MessageResponse
                        (false, "User mailVerificationUser Failed || Issue at UserId not found");
            }
            return new MessageResponse
                    (false, "User mailVerificationUser Failed || emailPasscode not match");
        }
    }

    public MessageResponse userNameExist(String username) {
        if(goPhygitalDao.existsByUsername(username)) {
            return new MessageResponse(true, "Username is exist");
        }
        return new MessageResponse(false, "Username is not exist");
    }

    public MessageResponse checkUserName(String username) {
        User user = goPhygitalDao.getUserForUsername(username);
        if(null != user) {
            if(user.isActive()) {
                String passCode = Utility.getUniqueEmailVerificationCode();
                UserEmailVerification userEmailVerification = new UserEmailVerification();
                userEmailVerification.setUserId(user.getUserId());
                userEmailVerification.setVerificationCode(passCode);
                userEmailVerification.setVerificationStatus(false);
                userEmailVerification.setPurpose(FETCH_PURPOSE);
                goPhygitalDao.saveUserEmailVerification(userEmailVerification);

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
        String link = applicationConfiguration.applicationUrl
                                        + "/api/auth/mailVerificationUser/" + userId + "/" + passCode;

        Properties prop = resourceConfiguration.readPropertyFile();
        emailRequest.setSubject(prop.getProperty(subject));
        emailRequest.setBody(prop.getProperty(body));
        emailRequest.parseTo(name, userName, passCode, link);

        return emailRequest;
    }

    public MessageResponse mailVerificationUserGetUserData(String username, String purpose, String passcode) {
        User user = goPhygitalDao.getUserForUsername(username);
        if(null != user) {
            MessageResponse messageResponse = mailVerificationUser(user.getUserId(), purpose, passcode);
            if(messageResponse.isFlag()){
                messageResponse.setObject(new UserVO(user));
            }
            return messageResponse;
        }
        return new MessageResponse(false, "Username not found.");
    }

    public MessageResponse authenticateRequest(HttpServletRequest request) {
        MessageResponse messageResponse = new MessageResponse();

        String username = goPhygitalService.getUsernameForJwtToken(request);
        UsernamePasswordAuthenticationToken authentication =
                goPhygitalService.authenticationJwtToken(request, username);

        if(null != authentication && authentication.isAuthenticated()) {
            User user = goPhygitalDao.getUserForUsername(username);
            if(null != user) {
                messageResponse.setFlag(true);
                messageResponse.setObject(new UserVO(user));
            }
        }
        else {
            messageResponse.setFlag(false);
            messageResponse.setMessage("Authorization token is not valid");
        }
        return messageResponse;
    }
}
