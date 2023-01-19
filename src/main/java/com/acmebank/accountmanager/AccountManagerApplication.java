package com.acmebank.accountmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class AccountManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountManagerApplication.class, args);
	}
}
