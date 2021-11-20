//package com.paymybuddy.api.security;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.paymybuddy.api.model.AppUser;
//
//public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//	private AuthenticationManager authenticationManager;
//
//	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
//		super();
//		this.authenticationManager = authenticationManager;
//	}
//
//	@Override
//	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//			throws AuthenticationException {
//		AppUser appUser = null;
//		try {
//			appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		} 
//		System.out.println("------------------------");
//		System.out.println("username :" + appUser.getEmail());
//		System.out.println("username :" + appUser.getPassword());
//		System.out.println("------------------------");
//		return authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(appUser.getEmail(),appUser.getPassword()));
//	}
//
////	@Override
////	public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
////			Authentication authResult) throws IOException, ServletException {
////		super.successfulAuthentication(request, response, chain, authResult);
////	}
//}