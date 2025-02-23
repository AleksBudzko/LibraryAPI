package com.bookstorage.controller;

import com.bookstorage.dto.BookDTO;
import com.bookstorage.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void testGetAllBooks() throws Exception {
        BookDTO book = BookDTO.builder()
                .id(1L)
                .isbn("1234567890")
                .title("Test Book")
                .genre("Fiction")
                .description("Test Desc")
                .author("Test Author")
                .build();

        when(bookService.getAllBooks()).thenReturn(Collections.singletonList(book));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("1234567890"));
    }

    @Test
    void testAddBook() throws Exception {
        BookDTO book = BookDTO.builder()
                .isbn("1234567890")
                .title("Test Book")
                .genre("Fiction")
                .description("Test Desc")
                .author("Test Author")
                .build();

        // При добавлении возвращаем BookDTO с id=1
        BookDTO createdBook = BookDTO.builder()
                .id(1L)
                .isbn("1234567890")
                .title("Test Book")
                .genre("Fiction")
                .description("Test Desc")
                .author("Test Author")
                .build();

        when(bookService.addBook(Mockito.any(BookDTO.class))).thenReturn(createdBook);

        String requestBody = """
                {
                    "isbn": "1234567890",
                    "title": "Test Book",
                    "genre": "Fiction",
                    "description": "Test Desc",
                    "author": "Test Author"
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
