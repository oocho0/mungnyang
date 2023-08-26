package com.mungnyang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MungnyangApplication {

	public static void main(String[] args) {
		SpringApplication.run(MungnyangApplication.class, args);
	}

}
