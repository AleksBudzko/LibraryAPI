package com.bookstorage.service;

import com.bookstorage.dto.BookDTO;
import com.bookstorage.exception.BookNotFoundException;
import com.bookstorage.mapper.BookMapper;
import com.bookstorage.model.Book;
import com.bookstorage.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
	private final BookRepository bookRepository;
	private final KafkaTemplate<String, Long> kafkaTemplate;
	private final BookMapper bookMapper = BookMapper.INSTANCE;

	public BookDTO addBook(BookDTO bookDTO) {
		Book book = bookMapper.toEntity(bookDTO);
		Book savedBook = bookRepository.save(book);

		// Асинхронное создание книги в book-tracker-service через Kafka
		kafkaTemplate.send("book_created", savedBook.getId());

		return bookMapper.toDTO(savedBook);
	}

	public List<BookDTO> getAllBooks() {
		return bookRepository.findByDeletedFalse()
				.stream()
				.map(bookMapper::toDTO)
				.toList();
	}

	public BookDTO getBookById(Long id) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException(id));
		return bookMapper.toDTO(book);
	}

	public BookDTO getBookByIsbn(String isbn) {
		return Optional.ofNullable(bookRepository.findByIsbn(isbn))
				.map(bookMapper::toDTO)
				.orElseThrow(() -> new BookNotFoundException(isbn));
	}

	public BookDTO updateBook(Long id, BookDTO updatedBookDTO) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException(id));

		book.setTitle(updatedBookDTO.getTitle());
		book.setGenre(updatedBookDTO.getGenre());
		book.setDescription(updatedBookDTO.getDescription());
		book.setAuthor(updatedBookDTO.getAuthor());

		return bookMapper.toDTO(bookRepository.save(book));
	}

	public void softDeleteBook(Long id) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException(id));

		book.setDeleted(true);
		bookRepository.save(book);

		// Асинхронное удаление книги в book-tracker-service через Kafka
		kafkaTemplate.send("book_deleted", id);
	}
}
