package com.bookstorage.controller;

import com.bookstorage.dto.BookDTO;
import com.bookstorage.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Добавить новую книгу")
    @ApiResponse(responseCode = "200", description = "Книга успешно добавлена")
    @PostMapping
    public BookDTO addBook(@RequestBody BookDTO bookDto) {
        return bookService.addBook(bookDto);
    }

    @Operation(summary = "Получить список всех книг")
    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(summary = "Получить книгу по ID")
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation(summary = "Получить книгу по ISBN")
    @GetMapping("/isbn/{isbn}")
    public BookDTO getBookByIsbn(@PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    @Operation(summary = "Обновить книгу")
    @PutMapping("/{id}")
    public BookDTO updateBook(@PathVariable Long id, @RequestBody BookDTO bookDto) {
        return bookService.updateBook(id, bookDto);
    }

    @Operation(summary = "Удалить книгу (soft delete)")
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.softDeleteBook(id);
    }
}
