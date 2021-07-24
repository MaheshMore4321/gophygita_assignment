package sd.sym.assignment.gophygita.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sd.sym.assignment.gophygita.dto.response.MessageResponse;
import sd.sym.assignment.gophygita.dto.response.UserVO;
import sd.sym.assignment.gophygita.service.ApplicationService;

import java.util.List;

import static sd.sym.assignment.gophygita.constant.ApplicationConstant.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(DASHBOARD_URL)
public class ApplicationController {

	@Autowired
	private ApplicationService applicationService;

	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = GET_DATA_USER_URL,
			    produces = MediaType.APPLICATION_JSON_VALUE)
	public UserVO getUserData(@PathVariable("userId") long userId) {
		return applicationService.getUserData(userId);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(path = GET_DATA_ADMIN_URL,
			    produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserVO> getAdminData() {
		return applicationService.getAdminData();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(path = TOGGLE_USER_ACTIVE,
			     produces = MediaType.APPLICATION_JSON_VALUE)
	public MessageResponse toggleUserActive(@PathVariable("userId") long userId) {
		return applicationService.toggleUserActive(userId);
	}
}