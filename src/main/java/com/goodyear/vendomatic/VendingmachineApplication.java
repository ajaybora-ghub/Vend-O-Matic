package com.goodyear.vendomatic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class VendingmachineApplication {

	public static void main(String[] args) {
		LOG.info("******* Starting VEND-O-MATIC *******");
		SpringApplication.run(VendingmachineApplication.class, args);
	}

	
}
