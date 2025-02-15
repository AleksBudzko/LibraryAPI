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

import java.util.List;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    public void testAddBook() throws Exception {
        BookDTO book = BookDTO.builder()
                .id(1L)
                .isbn("1234567890")
                .title("Test Book")
                .genre("Fiction")
                .description("Test description")
                .author("Test Author")
                .build();

        when(bookService.addBook(Mockito.any(BookDTO.class))).thenReturn(book);

        String json = "{\"isbn\":\"1234567890\", \"title\":\"Test Book\", \"genre\":\"Fiction\", \"description\":\"Test description\", \"author\":\"Test Author\"}";

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("1234567890"));
    }

    @Test
    public void testGetAllBooks() throws Exception {
        BookDTO book = BookDTO.builder()
                .id(1L)
                .isbn("1234567890")
                .title("Test Book")
                .genre("Fiction")
                .description("Test description")
                .author("Test Author")
                .build();
        when(bookService.getAllBooks()).thenReturn(List.of(book));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("1234567890"));
    }

    @Test
    public void testGetBookById() throws Exception {
        BookDTO book = BookDTO.builder()
                .id(1L)
                .isbn("1234567890")
                .title("Test Book")
                .genre("Fiction")
                .description("Test description")
                .author("Test Author")
                .build();
        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("1234567890"));
    }

    @Test
    public void testGetBookByIsbn() throws Exception {
        BookDTO book = BookDTO.builder()
                .id(1L)
                .isbn("1234567890")
                .title("Test Book")
                .genre("Fiction")
                .description("Test description")
                .author("Test Author")
                .build();
        when(bookService.getBookByIsbn("1234567890")).thenReturn(book);

        mockMvc.perform(get("/books/isbn/1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("1234567890"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        BookDTO updatedBook = BookDTO.builder()
                .id(1L)
                .isbn("1234567890")
                .title("Updated Title")
                .genre("Non-Fiction")
                .description("Updated description")
                .author("Updated Author")
                .build();
        when(bookService.updateBook(Mockito.eq(1L), Mockito.any(BookDTO.class))).thenReturn(updatedBook);

        String json = "{\"title\":\"Updated Title\", \"genre\":\"Non-Fiction\", \"description\":\"Updated description\", \"author\":\"Updated Author\"}";

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        // Так как метод удаления возвращает void, достаточно проверить статус 200 (OK)
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk());
    }
}
