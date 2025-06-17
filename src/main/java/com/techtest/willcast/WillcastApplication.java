package com.techtest.willcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WillcastApplication {

	public static void main(String[] args) {
		SpringApplication.run(WillcastApplication.class, args);
	}

}
