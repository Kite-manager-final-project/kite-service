package com.iron.kite_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KiteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KiteServiceApplication.class, args);
	}

}
