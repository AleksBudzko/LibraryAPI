package com.bookstorage.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // Используем аннотацию @Data от Lombok для автоматической генерации геттеров и сеттеров
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String genre;

    private String description;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private boolean deleted = false;
}