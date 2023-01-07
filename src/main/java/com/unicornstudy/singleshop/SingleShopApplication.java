package com.unicornstudy.singleshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SingleShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SingleShopApplication.class, args);
	}

}
