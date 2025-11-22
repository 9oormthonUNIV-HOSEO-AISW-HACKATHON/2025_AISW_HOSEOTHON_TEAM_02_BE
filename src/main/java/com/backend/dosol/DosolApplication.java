package com.backend.dosol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DosolApplication {

	public static void main(String[] args) {
		SpringApplication.run(DosolApplication.class, args);
	}

}
