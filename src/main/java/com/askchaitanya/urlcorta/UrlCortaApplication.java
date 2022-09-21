package com.askchaitanya.urlcorta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UrlCortaApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlCortaApplication.class, args);
	}

}
