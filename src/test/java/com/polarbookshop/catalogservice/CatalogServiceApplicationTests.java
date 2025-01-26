package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setup() {
        Book bookToUpdate = new Book("1231231232", "Title to update", "Author to update", 9.90);
        bookRepository.save(bookToUpdate);
    }


    @Test
    void whenPostRequestThenBookCreated() {
        var expectedBook = new Book("1231231231", "Title", "Author", 9.90);

        webTestClient
                .post()
                .uri("/books")
                .bodyValue(expectedBook)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class).value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.isbn())
                            .isEqualTo(expectedBook.isbn());
        });
    }

    @Test
    void whenPutRequestThenBookIsUpdated() {
        Book bookForPut = new Book("1231231232", "Title is putted", "Author is putted", 9.90);
        webTestClient
                .put()
                .uri("/books/1231231232")
                .bodyValue(bookForPut)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class).value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.isbn()).isEqualTo(bookForPut.isbn());
                    assertThat(actualBook.title()).isEqualTo(bookForPut.title());
                    assertThat(actualBook.author()).isEqualTo(bookForPut.author());
                    assertThat(actualBook.price()).isEqualTo(bookForPut.price());
                });
    }

}
