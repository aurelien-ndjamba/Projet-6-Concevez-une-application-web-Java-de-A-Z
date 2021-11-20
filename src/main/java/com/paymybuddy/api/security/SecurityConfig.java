package com.paymybuddy.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
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
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserDetailsService userDetailsService;
	// @Autowired // -> JDBCAUTHENTICATION Source de donnée injectée comme bean
	// private DataSource dbPayMyBuddy;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// INMEMORYAUTHENTICATION
//		auth.inMemoryAuthentication().withUser("user").password(bCryptPasswordEncoder.encode("password")).roles("USER");
//		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
//		auth.inMemoryAuthentication().withUser("user").password(bCryptPasswordEncoder.encode("password")).roles("USER");
//		auth.inMemoryAuthentication().withUser("user").password("{BCrypt}password").roles("USER");
		// JDBCAUTHENTICATION
//		auth.jdbcAuthentication().dataSource(dbPayMyBuddy).usersByUsernameQuery(
//				"select email as principal, password as credentials, active as active from appuser where email=?")
//				.authoritiesByUsernameQuery("SELECT email as principal, rolename as role FROM userRole WHERE email=?")
//				.passwordEncoder(bCryptPasswordEncoder);

		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	/*
	 * Pour gérer nos filtres, nous avons besoin de declarer la méthode configure
	 * qui prend en entrée un requete http - HttpSecurity -> correspond au requete
	 * http entrante 
	 * 
	 * @Override -> Pour que notre méthode prenne le pas sur celle de
	 * spring security
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
////		super.configure(http);
		http.csrf().disable();
		http.formLogin();
//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
////		http.httpBasic();

//		http.authorizeRequests().antMatchers("/users/save","/users/delete**").permitAll();
//		http.addFilter(new JWTAuthenticationFilter(authenticationManager()));
//		http.authorizeRequests().antMatchers(HttpMethod.GET,"/accounts/all").hasRole("ADMIN");
//		http.authorizeRequests().antMatchers(HttpMethod.GET,"/accounts/account").hasRole("ADMIN");
//		http.authorizeRequests().anyRequest().authenticated(); // toute les ressources necessite une authentificaiton

		// http.authorizeRequests().antMatchers("/accounts/delete**").hasRole("ADMIN");
////		http.authorizeRequests().antMatchers("/accounts/delete**").permitAll(); toute les requetes machantes sont autorisé
//		http.exceptionHandling().accessDeniedPage("/notAuthorized");

//		// Pour gérer nos filtres, nous avons besoin de declarer la méthode configure
//		// qui prend en entrée un requete http - HttpSecurity -> correspond au requete
//		// http entrante
		//
//		@Override // Pour que notre méthode prenne le pas sur celle de spring security
//		public void configure(HttpSecurity http) throws Exception {
//			
//			http.authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN").and().formLogin(); //GOOD
//			http.authorizeRequests().antMatchers("/user/**").hasAuthority("USER").and().formLogin();//GOOD

//			http.formLogin(); // loginFocrm() -> Methode Spring security pour créer une page de connexion par
		// défaut, vous évitant ainsi de la créer vous-même
//			http.authorizeRequests().anyRequest().permitAll(); //OK sans login
//			http.authorizeRequests().anyRequest().denyAll(); //OK error 403
//			http.authorizeRequests().antMatchers("/**").authenticated(); //OK error 403
//			http.authorizeRequests().antMatchers("/**").authenticated().and().formLogin(); //OK
//			http.authorizeRequests().anyRequest().authenticated(); //OK error 403
//			http.authorizeRequests().anyRequest().authenticated().and().formLogin(); //OK
//			http.authorizeRequests().anyRequest().hasRole("ADMIN"); //OK error 403
//			http.authorizeRequests().anyRequest().hasRole("ADMIN").and().formLogin(); //OK error 403 for USER
//			http.authorizeRequests().anyRequest().hasRole("USER").and().formLogin(); //error 403 for USER ???
//			http.authorizeRequests().anyRequest().hasAuthority("USER").and().formLogin(); //OK for USER
//			http.authorizeRequests().anyRequest().hasAuthority("USER").and().formLogin(); //OK for macron who USER & ADMIN
//			http.authorizeRequests().anyRequest().hasAuthority("ADMIN").and().formLogin(); //OK error 403 for USER
//			http.authorizeRequests().anyRequest().hasAuthority("ADMIN").and().formLogin(); //OK for ADMIN
//			http.csrf().disable();
	}

}
