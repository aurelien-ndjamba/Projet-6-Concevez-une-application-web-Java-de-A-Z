package com.paymybuddy.webapp.controller;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paymybuddy.webapp.model.Friend;
import com.paymybuddy.webapp.service.FriendService;

import lombok.Data;

@Data
@Controller 
public class FriendController {

	@Autowired
	private FriendService friendService; 



}
