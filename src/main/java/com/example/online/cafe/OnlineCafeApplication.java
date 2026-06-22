package com.example.online.cafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OnlineCafeApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineCafeApplication.class, args);
	}

}
