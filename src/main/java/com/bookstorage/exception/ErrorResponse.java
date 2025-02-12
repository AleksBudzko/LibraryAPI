package com.bookstorage.exception; // Или com.bookstorage.model

public class ErrorResponse {
    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Геттеры
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}