package sd.sym.assignment.gophygita.configuration.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sd.sym.assignment.gophygita.service.GoPhygitalService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private GoPhygitalService goPhygitalService;

	@Override
	protected void doFilterInternal
		(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		SecurityContextHolder.getContext()
			.setAuthentication(goPhygitalService.authenticationJwtToken(request));
		filterChain.doFilter(request, response);
	}
}