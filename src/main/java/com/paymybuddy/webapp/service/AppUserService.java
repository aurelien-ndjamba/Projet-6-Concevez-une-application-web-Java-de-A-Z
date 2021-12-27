package com.paymybuddy.webapp.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.model.AppUser;
import com.paymybuddy.webapp.repository.AppUserProxy;

import lombok.Data;

@Data
@Service
public class AppUserService {      

	@Autowired
	private AppUserProxy appUserProxy;  

	public AppUser findByEmail(String email) {
			AppUser appUser = appUserProxy.findByEmail(email);
		return appUser;
	}
	
	public ArrayList<String> findOtherEmailsFriendsAvailableForThisEmail(String email) {
		return appUserProxy.findOtherEmailsFriendsAvailableForThisEmail(email);
	}

	public AppUser updatePassword(String email, String password) {
		return appUserProxy.updatePassword(email, password);
	}

	public AppUser updatePhone(String email, int phone) {
		return appUserProxy.updatePhone(email, phone);
	}

	public AppUser save(AppUser appUser) {
		return appUserProxy.save(appUser);
		
	}

	public AppUser deleteAppUser(String email) {
		return appUserProxy.deleteAppUser(email);
	}

}