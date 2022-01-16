package com.swashtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class SwashtechRushmgmtServiceApplication {

	public static void main(String[] args) {
		System.err.print("Starting SpringBoot");
		ConfigurableApplicationContext context = SpringApplication.run(SwashtechRushmgmtServiceApplication.class, args);
		ConfigurableEnvironment env = context.getEnvironment();
		System.err.println("MongoDB URL -------> : "+env.getProperty("spring.data.mongodb.uri"));
	}

}
