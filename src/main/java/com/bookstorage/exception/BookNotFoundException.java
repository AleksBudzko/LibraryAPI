package com.bookstorage.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Book with ID " + id + " not found.");
    }

    public BookNotFoundException(String isbn) {
        super("Book with ISBN " + isbn + " not found.");
    }
}