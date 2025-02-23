package com.bookstorage.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BearerTokenAuthenticationFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private BearerTokenAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new BearerTokenAuthenticationFilter();
        SecurityContextHolder.clearContext();
    }

    @Test
    void testValidToken() throws IOException, jakarta.servlet.ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer mysecrettoken");

        filter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testInvalidToken() throws IOException, jakarta.servlet.ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidtoken");

        filter.doFilterInternal(request, response, filterChain);

        // Аутентификация не установлена
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testNoHeader() throws IOException, jakarta.servlet.ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        // Аутентификация не установлена
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
