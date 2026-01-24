package com.example.crossPlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CrossPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrossPlatformApplication.class, args);
	}

}
