package com.bookstorage.service;

import com.bookstorage.dto.BookDTO;
import com.bookstorage.exception.BookNotFoundException;
import com.bookstorage.mapper.BookMapper;
import com.bookstorage.model.Book;
import com.bookstorage.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private KafkaTemplate<String, Long> kafkaTemplate;

    // Внедряем BookMapper через конструктор, чтобы можно было его замокать
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;
    private BookDTO sampleBookDTO;

    @BeforeEach
    void setUp() {
        sampleBook = Book.builder()
                .id(1L)
                .isbn("1234567890")
                .title("Test Title")
                .genre("Fiction")
                .description("Test description")
                .author("Test Author")
                .deleted(false)
                .build();

        sampleBookDTO = BookDTO.builder()
                .id(1L)
                .isbn("1234567890")
                .title("Test Title")
                .genre("Fiction")
                .description("Test description")
                .author("Test Author")
                .build();

        doReturn(sampleBook).when(bookMapper).toEntity(any(BookDTO.class));
        doReturn(sampleBookDTO).when(bookMapper).toDTO(any(Book.class));
        doReturn(sampleBook).when(bookRepository).save(any(Book.class));
    }

    @Test
    void testAddBook() {
        BookDTO result = bookService.addBook(sampleBookDTO);
        assertNotNull(result);
        assertEquals("1234567890", result.getIsbn());
        verify(kafkaTemplate, times(1)).send("book_created", sampleBook.getId());
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findByDeletedFalse()).thenReturn(Collections.singletonList(sampleBook));
        List<BookDTO> books = bookService.getAllBooks();
        assertEquals(1, books.size());
        assertEquals("1234567890", books.get(0).getIsbn());
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        BookDTO result = bookService.getBookById(1L);
        assertEquals("1234567890", result.getIsbn());
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(2L));
    }

    @Test
    void testGetBookByIsbn() {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(sampleBook);
        BookDTO result = bookService.getBookByIsbn("1234567890");
        assertEquals("1234567890", result.getIsbn());
    }

    @Test
    void testGetBookByIsbn_NotFound() {
        when(bookRepository.findByIsbn("0000000000")).thenReturn(null);
        assertThrows(BookNotFoundException.class, () -> bookService.getBookByIsbn("0000000000"));
    }

    @Test
    void testUpdateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        BookDTO updatedDTO = BookDTO.builder()
                .title("New Title")
                .genre("Non-Fiction")
                .description("New description")
                .author("New Author")
                .build();

        BookDTO result = bookService.updateBook(1L, updatedDTO);
        assertEquals("New Title", result.getTitle());
    }

    @Test
    void testSoftDeleteBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        bookService.softDeleteBook(1L);
        verify(kafkaTemplate, times(1)).send("book_deleted", 1L);
        assertTrue(sampleBook.isDeleted());
    }
}
