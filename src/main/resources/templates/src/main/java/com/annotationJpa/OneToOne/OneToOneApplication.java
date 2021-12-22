package com.annotationJpa.OneToOne;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OneToOneApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(OneToOneApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		EntityManagerFactory entityManagerFactory = null;
//		EntityManager entityManager = null;
//		entityManagerFactory = Persistence.createEntityManagerFactory("WebStore");
//		entityManager = entityManagerFactory.createEntityManager();

//		EntityTransaction trans = entityManager.getTransaction();
//		trans.begin();

//		Account acc = new Account();
//		System.out.println(Account);

	}

}
