package com.paymybuddy.api.security;

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

//	@Autowired  // -> JDBCAUTHENTICATION Source de donnée injectée comme bean
//	public void globalConfig(AuthenticationManagerBuilder auth) throws Exception { //, DataSource dataSource
	// --------------------- INMEMORY AUTHENTICATION 1--------------------- OK
//		auth.inMemoryAuthentication().withUser("user").password(bCryptPasswordEncoder.encode("password")).roles("USER");
//
	// --------------------- JDBC AUTHENTICATION 1---------------------
//		auth.jdbcAuthentication()
//		.dataSource(dataSource)
//		.usersByUsernameQuery(
//				"select email as principal, password as credentials, active as active from appuser where email=?") 
//		        // active as active = true
//		.authoritiesByUsernameQuery("select email as principal, rolename as role from userrole WHERE email=?")
//		.rolePrefix("ROLE_")
////		.passwordEncoder(bCryptPasswordEncoder)
//		;
	// --------------------- USERDETAILSSERVICE AUTHENTICATION 1
	// ---------------------
//	auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
//	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception { // , DataSource dataSource
		// --------------------- INMEMORY AUTHENTICATION 2--------------------- OK
//		auth.inMemoryAuthentication().withUser("user").password(bCryptPasswordEncoder.encode("password")).roles("USER");
//		auth.inMemoryAuthentication().withUser("admin").password(bCryptPasswordEncoder.encode("password")).roles("ADMIN","USER");
		// --------------------- JDBC AUTHENTICATION ---------------------
//		auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(
//				"select email as principal, password as credentials, active as active from appuser where email=?") 
		// active as active = true
//				.authoritiesByUsernameQuery("select email as principal, rolename as role from userrole WHERE email=?")
//				.rolePrefix("ROLE_")
//		.passwordEncoder(bCryptPasswordEncoder)
		;

		// --------------------- USERDETAILSSERVICE AUTHENTICATION ---------------------
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	/*
	 * Pour gérer nos filtres, nous avons besoin de declarer la méthode configure
	 * qui prend en entrée un requete http - HttpSecurity -> correspond au requete
	 * http entrante
	 * 
	 * @Override -> Pour que notre méthode prenne le pas sur celle de spring
	 * security
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http
//		.csrf().disable()
//		.authorizeRequests().antMatchers("/css/**", "/js/**", "/images/**").permitAll()
//		.anyRequest().authenticated().and().formLogin()
////				.loginPage("/login")
//				.defaultSuccessUrl("/index.html").failureUrl("/login").permitAll().and().logout()
//				.invalidateHttpSession(true).logoutUrl("/logout").permitAll();

		http.csrf().disable();
		http.authorizeRequests().antMatchers("/**").permitAll(); // Always OK mais NOK if method controller @Secured

//		http
////		.csrf().disable()
//		.authorizeRequests().antMatchers("/","home","/login").permitAll().and()
////		.authorizeRequests().anyRequest().authenticated(); // OK after authentification
//	 .authorizeRequests().anyRequest().authenticated().and().formLogin().permitAll();
	}

	
}
