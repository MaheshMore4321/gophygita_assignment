package sd.sym.assignment.gophygita.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sd.sym.assignment.gophygita.configuration.jwt.JwtUtils;
import sd.sym.assignment.gophygita.constant.ERole;
import sd.sym.assignment.gophygita.entity.Role;
import sd.sym.assignment.gophygita.entity.User;
import sd.sym.assignment.gophygita.repository.RoleRepository;
import sd.sym.assignment.gophygita.service.security.UserDetailsServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GoPhygitalService {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	public String getUsernameForJwtToken(HttpServletRequest request) {
		String jwt = parseJwt(request);
		if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
			return jwtUtils.getUserNameFromJwtToken(jwt);
		}
		return null;
	}

	public UsernamePasswordAuthenticationToken authenticationJwtToken(HttpServletRequest request) {
		return authenticationJwtToken(request, getUsernameForJwtToken(request));
	}

	public UsernamePasswordAuthenticationToken
							authenticationJwtToken(HttpServletRequest request, String username) {
		try {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails.getUsername(), null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			return authentication;
		} catch (Exception e) {
			log.error("Cannot set user authentication: {}", e);
		}
		return null;
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
	}
}