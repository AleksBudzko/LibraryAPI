package com.bookstorage.controller;

import com.bookstorage.dto.BookDTO;
import com.bookstorage.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDto) {
        return ResponseEntity.ok(bookService.addBook(bookDto));
    }

    @Operation(summary = "Получить список всех книг")
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @Operation(summary = "Получить книгу по ID")
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(summary = "Получить книгу по ISBN")
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBookByIsbn(isbn));
    }

    @Operation(summary = "Обновить книгу")
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDto) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDto));
    }

    @Operation(summary = "Удалить книгу (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.softDeleteBook(id);
        return ResponseEntity.ok().build();
    }
}
