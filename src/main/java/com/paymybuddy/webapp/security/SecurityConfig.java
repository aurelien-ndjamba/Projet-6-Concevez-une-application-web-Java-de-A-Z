package com.paymybuddy.webapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 * @Configuration -> Indique à srping boot qu'il s'agit d'une classe de config -
 * classe traité au démarrage de l'app
 * 
 * @EnableWebSecurity -> Indique à spring sécurity de savoir ou se trouve la
 * configuration web Etendre cette classe va permettre de gérer notre chaine de
 * filtre
 * 
 * WebSecurityConfigurerAdapter -> Pour personnaliser Spring sécurity, on a besoin d'hériter d'une classe
 *  WebSecurityConfigurerAdapter
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception { 
		// --------------------- USERDETAILSSERVICE AUTHENTICATION ---------------------
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
//		.csrf().disable()
				.authorizeRequests()
				.antMatchers("/css/**", "/js/**", "/images/**", "/", "/home", "/login", "/register", "/logoff",
						"/index","/contact","/profile","/transfer")
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/login")
				.defaultSuccessUrl("/home").failureUrl("/").permitAll().and().logout().invalidateHttpSession(true)
				.logoutUrl("/logout").permitAll(); 

//		http.csrf().disable();
//		http.authorizeRequests().antMatchers("/**").permitAll(); // Always OK mais NOK if method controller @Secured

	}

}
