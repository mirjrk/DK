package com.zesium.friendrecap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
* Main class
*
* @author  Katarina Zorbic
* @version 1.0
* @since   2017-02-01
*/ 
@SpringBootApplication
@EnableJpaRepositories
public class FriendRecapApplication {
	
	private FriendRecapApplication () {
		
	}

	public static void main(String[] args) {
		SpringApplication.run(FriendRecapApplication.class, args).close();
	}
}
