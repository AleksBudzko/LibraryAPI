package com.bookstorage.service;

import com.bookstorage.exception.BookNotFoundException;
import com.bookstorage.model.Book;
import com.bookstorage.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;

	public Book getBookById(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException(id));
	}

	public Book getBookByIsbn(String isbn) {
		return Optional.ofNullable(bookRepository.findByIsbn(isbn))
				.orElseThrow(() -> new BookNotFoundException(isbn));
	}
}