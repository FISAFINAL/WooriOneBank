package com.fisa.woorionebank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WoorionebankApplication {
	public static void main(String[] args) {
		SpringApplication.run(WoorionebankApplication.class, args);
	}
	
}
