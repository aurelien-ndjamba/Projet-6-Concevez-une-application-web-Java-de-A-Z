package com.paymybuddy.api.service;

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

import com.paymybuddy.api.model.AppUser;
import com.paymybuddy.api.model.Role;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired 
	UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		AppUser appUser = userService.findByEmail(email);
		if (appUser == null) throw new UsernameNotFoundException(email);
		Collection<GrantedAuthority> authorities = new ArrayList<>(); 
//		Collection<String> roles = new ArrayList<>(); 
		appUser.getRoles().forEach (r -> {
			authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
//			roles.add(r.getRoleName());
		});
		
		List<String> roles = new ArrayList<>();
		for (GrantedAuthority ga : authorities) {
			roles.add(ga.getAuthority());
		}
		 
		System.out.println(roles);
		System.out.println(new User(appUser.getEmail(),appUser.getPassword(),authorities));
		return new User(appUser.getEmail(),appUser.getPassword(),authorities);
	}

}
