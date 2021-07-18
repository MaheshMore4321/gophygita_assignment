package sd.sym.assignment.gophygita.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sd.sym.assignment.gophygita.dto.request.LoginRequest;
import sd.sym.assignment.gophygita.dto.request.RegistrationRequest;
import sd.sym.assignment.gophygita.dto.response.MessageResponse;
import sd.sym.assignment.gophygita.service.authentication.AuthenticationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static sd.sym.assignment.gophygita.constant.ApplicationConstant.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(AUTH_REQUEST_MAPPING_URL)
public class AuthenticationController {

	@Autowired
	AuthenticationService authenticationService;

	@PostMapping(path = AUTH_LOGIN_URL, consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
	public Object loginUser(@RequestBody LoginRequest loginRequest) {
		return authenticationService.loginUser(loginRequest);
	}

	@PostMapping(path = AUTH_REGISTRATION_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public MessageResponse registerUser(@RequestBody RegistrationRequest registrationRequest) {
		return authenticationService.registerUser(registrationRequest);
	}

	@GetMapping(path = AUTH_VERIFICATION_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public MessageResponse mailVerificationUser
			(@PathVariable("userId") long userId, @PathVariable("emailPasscode") String emailPasscode) {
		return authenticationService.mailVerificationUser(userId, LOGIN_PURPOSE, emailPasscode);
	}

	@GetMapping(path = AUTH_EXIST_USERNAME_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	public MessageResponse userNameExist(@RequestParam("username") String username) {
		return authenticationService.userNameExist(username.toLowerCase());
	}

	@GetMapping(path = AUTH_CHECK_USERNAME_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	public MessageResponse checkUserName(@RequestParam("username") String username) {
		return authenticationService.checkUserName(username);
	}

	@GetMapping(path = AUTH_CHECK_PASSCODE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	public MessageResponse passcodeVerificationUser(@RequestParam("username") String username, @RequestParam("emailPasscode") String emailPasscode) {
		return authenticationService.mailVerificationUser(username.toLowerCase(), FETCH_PURPOSE, emailPasscode);
	}

	@GetMapping(path = AUTH_CHECK_USERNAME_PASSCODE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getUserData(@RequestParam("username") String username, @RequestParam("passcode") String passcode) {
		return authenticationService.mailVerificationUserGetUserData(username.toLowerCase(), FETCH_PURPOSE, passcode);
	}

	@GetMapping(path = AUTH_CHECK_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object authenticateRequest(HttpServletRequest request, HttpServletResponse response) {
		return authenticationService.authenticateRequest(request, response);
	}
}
