package com.acmebank.accountmanager;

import com.acmebank.accountmanager.controller.AccountManagerRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class AccountManagerApplicationTests {

	@Autowired
	private AccountManagerRestController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}
}
