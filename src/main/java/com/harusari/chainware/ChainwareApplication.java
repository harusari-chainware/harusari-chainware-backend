package com.harusari.chainware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ChainwareApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChainwareApplication.class, args);
	}

}