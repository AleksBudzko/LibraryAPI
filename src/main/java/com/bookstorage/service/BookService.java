package com.bookstorage.service;

import com.bookstorage.model.Book;
import com.bookstorage.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;

	public Book addBook(Book book) {
		return bookRepository.save(book);
	}

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public Optional<Book> getBookById(Long id) {
		return bookRepository.findById(id);
	}

	public Book getBookByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	public Book updateBook(Long id, Book updatedBook) {
		return bookRepository.findById(id)
				.map(book -> {
					book.setTitle(updatedBook.getTitle());
					book.setGenre(updatedBook.getGenre());
					book.setDescription(updatedBook.getDescription());
					book.setAuthor(updatedBook.getAuthor());
					return bookRepository.save(book);
				})
				.orElseThrow(() -> new RuntimeException("Book not found"));
	}

	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}
}