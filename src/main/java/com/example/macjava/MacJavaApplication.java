package com.example.macjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MacJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MacJavaApplication.class, args);
	}

}
