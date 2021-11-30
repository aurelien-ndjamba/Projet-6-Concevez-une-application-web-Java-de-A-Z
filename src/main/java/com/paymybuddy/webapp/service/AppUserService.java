package com.paymybuddy.webapp.service;

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
    
    public Iterable<AppUser> getUsers() {
        return appUserProxy.getUsers();
    }
    
    public AppUser getUser(String email) {
        return appUserProxy.getUser(email);
    }
    
	public AppUser updatePassword(String email, String password) {
		return appUserProxy.updatePassword(email, password);
	}
    
}