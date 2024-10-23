package com.polarbookshop.catalogservice.persistence;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryBookRepository implements BookRepository {
    private static final Map<String, Book> bookMap = new ConcurrentHashMap<>();


    @Override
    public Iterable<Book> findAll() {
        bookMap.put("1", new Book("123","Waddup","Harding",2.50));
        return bookMap.values();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return existsByIsbn(isbn) ? Optional.of(bookMap.get(isbn)) :
                Optional.empty();
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return bookMap.get(isbn) != null;
    }

    @Override
    public Book save(Book book) {
        bookMap.put(book.isbn(), book);
        return book;
    }

    @Override
    public void deleteByIsbn(String isbn) {
        bookMap.remove(isbn);
    }
}
