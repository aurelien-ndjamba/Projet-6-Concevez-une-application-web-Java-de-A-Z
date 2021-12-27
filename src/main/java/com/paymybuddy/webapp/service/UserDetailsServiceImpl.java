package com.paymybuddy.webapp.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.model.AppUser;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired 
	AppUserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = userService.findByEmail(username);
		System.out.println(appUser);
		if (appUser.getPseudo() == null) throw new UsernameNotFoundException(username);
		Collection<GrantedAuthority> authorities = new ArrayList<>(); 
		appUser.getRoles().forEach (r -> {
			authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
		});
		
		List<String> roles = new ArrayList<>();
		for (GrantedAuthority ga : authorities) {
			roles.add(ga.getAuthority());
		}
		return new User(username,userService.findByEmail(username).getPassword(),authorities);
	}

}
