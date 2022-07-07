package com.testesapi.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.testesapi.domain.User;
import com.testesapi.repository.UserRepository;

@Configuration
@Profile("local")
public class LocalConfig {

	@Autowired
	private UserRepository repository;
	
	@Bean
	public void startDb() {
		User u1 = new User(1, "Patrick", "gigbby@gmail.com", "123");
		User u2 = new User(2, "Thais", "thais@gmail.com", "123");
		User u3 = new User(3, "Gabriel", "gabriels@gmail.com", "123");
		repository.saveAll(List.of(u1,u2,u3));
	}
	
}
