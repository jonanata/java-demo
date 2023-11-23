package com.ioc.iotserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IotserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotserverApplication.class, args);
	}

}
