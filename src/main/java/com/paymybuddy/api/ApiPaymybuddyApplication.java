package com.paymybuddy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.paymybuddy.api.repository.UserRepository;

@SpringBootApplication
public class ApiPaymybuddyApplication implements CommandLineRunner {
	
	@Autowired UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(ApiPaymybuddyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(userRepository.findById("nicolas.sarkozy@gmail.com").get().getPseudo());
		System.out.println(userRepository.findById("nicolas.sarkozy@gmail.com").get().getAccount());
		System.out.println(userRepository.findById("nicolas.sarkozy@gmail.com").get().getRoles());
	}
	
	/*
	 * Au démarrage de l'app, toute les méthodes avec annotation Bean sont exécuté
	 * et les resultats retournés deviennent un bean spring et comme ça devient un
	 * bean spring on peut l'injecter partout
	 * 
	 * Un methode annotée @Bean signifie que l'objet retourné par cette méthode sera
	 * dans le contexte de l'application. Il devient un composant Spring et du coup
	 * on peut l'injecter
	 */
	@Bean // pour pouvoir utiliser le même objet dans les autres composants de
			// l'application
	public BCryptPasswordEncoder getBCPE() {
		return new BCryptPasswordEncoder(16);
	}


}
