package com.book.storageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.book.controller",
		"com.book.service",
		"com.book.repository",
		"com.book.model",
		"com.book.config"
})
public class BookStorageServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookStorageServiceApplication.class, args);
	}
}
