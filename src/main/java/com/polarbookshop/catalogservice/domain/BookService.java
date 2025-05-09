package com.polarbookshop.catalogservice.domain;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> viewBookList() {
        //  return bookRepository.findAll();
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .map(book -> new Book(
                        book.getId(),
                        "Bawling like a kid",
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPrice(),
                        book.getPublisher(),
                        book.getCreatedDate(),
                        book.getLastModifiedDate(),
                        book.getVersion()))
                .toList();
    }

    public Book viewBookDetails(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public Book addBookToCatalog(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BookAlreadyExistsException(book.getIsbn());
        }
        return bookRepository.save(book);
    }

    public void removeBookFromCatalog(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }

    public Book editBookDetails(String isbn, Book book) {
        return bookRepository.findByIsbn(isbn)
                .map(existingBook -> {
                    var bookToUpdate = new Book(
                            existingBook.getId(),
                            existingBook.getIsbn(),
                            book.getTitle(),
                            book.getAuthor(),
                            book.getPrice(),
                            book.getPublisher(),
                            existingBook.getCreatedDate(),
                            existingBook.getLastModifiedDate(),
                            existingBook.getVersion());
                    return bookRepository.save(bookToUpdate);
                })
                .orElseGet(() -> addBookToCatalog(book));
    }
}
