package com.annotationJpa.OneToOne;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppUserController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired 
	private AppUserRepository appUserRepository;
	
	@GetMapping("/users/all")
	public List<AppUser> findAll() {
		logger.info("INFO: Liste tous les utilisateurs de l'application sans leur mot de passe");
		return appUserRepository.findAll();
	}
	
	@RequestMapping(value = "/users/user", method = RequestMethod.GET, params = { "email" })
	public AppUser findByEmail(String email) {
		logger.info("INFO: Liste des infos d'un utilisateur ayant l'email donné en parametre.");
		return appUserRepository.findByEmail(email);
	}

}
