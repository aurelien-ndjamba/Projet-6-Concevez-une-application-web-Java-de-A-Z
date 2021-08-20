package com.paymybuddy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.paymybuddy.api.repository.UserRepository;

import lombok.Data;

//@EnableJpaRepositories("com.paymybuddy.*")
//@ComponentScan(basePackages = { "com.paymybuddy.model.*" })
//@EntityScan("com.paymybuddy.*") 
//@SpringBootApplication(scanBasePackages="com.paymybuddy")
@Data
@SpringBootApplication
public class ApiPaymybuddyApplication implements CommandLineRunner{

	@Autowired UserRepository userRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(ApiPaymybuddyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}

}
