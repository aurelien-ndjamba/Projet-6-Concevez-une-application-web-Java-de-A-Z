//package com.paymybuddy.api.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter{
//	
//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
//	@Autowired
//	private UserDetailsService userDetailsService;
//	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
////		super.configure(auth);
////		PasswordEncoder passwordEncoder = passwordEncoder();
////		System.out.println("--------------------------------------");
////		System.out.println(bCryptPasswordEncoder.encode("SarkozyN"));
////		System.out.println(bCryptPasswordEncoder.encode("HollandeF"));
////		System.out.println(bCryptPasswordEncoder.encode("MacronN"));
////		System.out.println(bCryptPasswordEncoder.encode("ChiracJ"));
////		System.out.println(bCryptPasswordEncoder.encode("MitterandF"));
////		System.out.println(bCryptPasswordEncoder.encode("PompidouG"));
////		System.out.println("--------------------------------------");
////		auth.inMemoryAuthentication().withUser("user1").password(passwordEncoder.encode("123")).roles("USER");
////		auth.inMemoryAuthentication().withUser("aurelien").password(passwordEncoder.encode("cmr")).roles("USER","ADMIN");
//	
//		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
//	}
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception{
//////		super.configure(http);
//		http.csrf().disable();
//		http.formLogin();
////		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//////		http.httpBasic();
//		
////		http.authorizeRequests().antMatchers("/users/save","/users/delete**").permitAll();
////		http.addFilter(new JWTAuthenticationFilter(authenticationManager()));
////		http.authorizeRequests().antMatchers(HttpMethod.GET,"/accounts/all").hasRole("ADMIN");
////		http.authorizeRequests().antMatchers(HttpMethod.GET,"/accounts/account").hasRole("ADMIN");
////		http.authorizeRequests().anyRequest().authenticated(); // toute les ressources necessite une authentificaiton
//
//		//		http.authorizeRequests().antMatchers("/accounts/delete**").hasRole("ADMIN");
//////		http.authorizeRequests().antMatchers("/accounts/delete**").permitAll(); toute les requetes machantes sont autorisé
////		http.exceptionHandling().accessDeniedPage("/notAuthorized");
//	}
//	
//}
