package com.hanghae.lookAtMyCat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LookAtMyCatApplication {

	public static void main(String[] args) {
		SpringApplication.run(LookAtMyCatApplication.class, args);
	}

}
