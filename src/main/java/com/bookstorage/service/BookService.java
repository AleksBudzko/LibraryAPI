package com.bookstorage.service;

import com.bookstorage.exception.BookNotFoundException;
import com.bookstorage.model.Book;
import com.bookstorage.repository.BookRepository;
import com.bookstorage.service.client.BookTrackerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
	private final BookRepository bookRepository;
	private final BookTrackerClient bookTrackerClient;

	public Book addBook(Book book) {
		Book savedBook = bookRepository.save(book);
		bookTrackerClient.createTracker(savedBook.getId());
		return savedBook;
	}

	public List<Book> getAllBooks() {
		return bookRepository.findByDeletedFalse();
	}

	public Book getBookById(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException(id));
	}

	public Book getBookByIsbn(String isbn) {
		return Optional.ofNullable(bookRepository.findByIsbn(isbn))
				.orElseThrow(() -> new BookNotFoundException(isbn));
	}

	public Book updateBook(Long id, Book updatedBook) {
		return bookRepository.findById(id)
				.map(book -> {
					updateBookFields(book, updatedBook);
					return bookRepository.save(book);
				})
				.orElseThrow(() -> new BookNotFoundException(id));
	}

	private void updateBookFields(Book target, Book source) {
		target.setTitle(source.getTitle());
		target.setGenre(source.getGenre());
		target.setDescription(source.getDescription());
		target.setAuthor(source.getAuthor());
	}

	public void deleteBook(Long id) {
		bookRepository.findById(id).ifPresent(book -> {
			book.setDeleted(true);
			bookRepository.save(book);
			bookTrackerClient.deleteTracker(id);
		});
	}
}

