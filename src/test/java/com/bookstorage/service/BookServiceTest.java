package com.bookstorage.service;

import com.bookstorage.dto.BookDTO;
import com.bookstorage.exception.BookNotFoundException;
import com.bookstorage.model.Book;
import com.bookstorage.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private KafkaTemplate<String, Long> kafkaTemplate;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;
    private BookDTO sampleBookDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleBook = Book.builder()
                .id(1L)
                .isbn("1234567890")
                .title("Test Book")
                .genre("Fiction")
                .description("Test description")
                .author("Test Author")
                .deleted(false)
                .build();

        sampleBookDTO = BookDTO.builder()
                .id(1L)
                .isbn("1234567890")
                .title("Test Book")
                .genre("Fiction")
                .description("Test description")
                .author("Test Author")
                .build();
    }

    @Test
    public void testAddBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        BookDTO result = bookService.addBook(sampleBookDTO);

        verify(kafkaTemplate, times(1)).send("book_created", sampleBook.getId());
        assertNotNull(result);
        assertEquals(sampleBookDTO.getIsbn(), result.getIsbn());
    }

    @Test
    public void testGetAllBooks() {
        when(bookRepository.findByDeletedFalse()).thenReturn(Collections.singletonList(sampleBook));

        var books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals(sampleBook.getIsbn(), books.get(0).getIsbn());
    }

    @Test
    public void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        BookDTO result = bookService.getBookById(1L);
        assertNotNull(result);
        assertEquals(sampleBook.getIsbn(), result.getIsbn());
    }

    @Test
    public void testGetBookById_NotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(2L));
    }

    @Test
    public void testGetBookByIsbn() {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(sampleBook);

        BookDTO result = bookService.getBookByIsbn("1234567890");
        assertNotNull(result);
        assertEquals(sampleBook.getIsbn(), result.getIsbn());
    }

    @Test
    public void testGetBookByIsbn_NotFound() {
        when(bookRepository.findByIsbn("0987654321")).thenReturn(null);
        assertThrows(BookNotFoundException.class, () -> bookService.getBookByIsbn("0987654321"));
    }

    @Test
    public void testUpdateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        BookDTO updatedDTO = BookDTO.builder()
                .title("Updated Title")
                .genre("Non-Fiction")
                .description("Updated description")
                .author("Updated Author")
                .build();

        BookDTO result = bookService.updateBook(1L, updatedDTO);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    public void testSoftDeleteBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        bookService.softDeleteBook(1L);

        verify(kafkaTemplate, times(1)).send("book_deleted", 1L);
    }
}
