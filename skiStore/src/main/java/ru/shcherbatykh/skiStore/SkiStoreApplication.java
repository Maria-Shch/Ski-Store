package ru.shcherbatykh.skiStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SkiStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkiStoreApplication.class, args);
	}

}
