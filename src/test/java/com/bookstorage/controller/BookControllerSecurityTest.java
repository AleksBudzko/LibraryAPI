package com.bookstorage.controller;

import com.bookstorage.config.SecurityConfig;
import com.bookstorage.dto.BookDTO;
import com.bookstorage.security.BearerTokenAuthenticationFilter;
import com.bookstorage.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = BookController.class)
@Import({SecurityConfig.class, BearerTokenAuthenticationFilter.class})
class BookControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    /**
     * Без токена получаем 401 Unauthorized
     */
    @Test
    void testGetAllBooksWithoutToken() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * С валидным токеном "Bearer mysecrettoken" получаем 200 OK
     */
    @Test
    void testGetAllBooksWithValidToken() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/books")
                        .header("Authorization", "Bearer mysecrettoken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
