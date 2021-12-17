package com.paymybuddy.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.webapp.model.AppUser;
import com.paymybuddy.webapp.model.Friend;
import com.paymybuddy.webapp.repository.AppUserProxy;

import lombok.Data;

@Data
@Service
public class CommunService {
	
	@Autowired
	private AppUserProxy appUserProxy;

//    public Friend checkingAccess(AppUser appUser) {
//        return appUserProxy.checkingAccess(appUser);
//    }
	
}
