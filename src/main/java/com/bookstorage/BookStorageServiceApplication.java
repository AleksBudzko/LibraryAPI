package com.bookstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.bookstorage"})
public class BookStorageServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookStorageServiceApplication.class, args);
	}
}
