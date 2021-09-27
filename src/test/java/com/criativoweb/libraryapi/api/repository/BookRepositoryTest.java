package com.criativoweb.libraryapi.api.repository;

import com.criativoweb.libraryapi.api.model.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retorna verdadeiro quando existir um livro na base com isbn informado")
    public void returnTrueWhenIsbnExists(){
        String isbn = "123";
        Book book = createNewBook(isbn);
        entityManager.persist(book);
        boolean exists = repository.existsByIsbn(isbn);

        assertThat(exists).isTrue();
    }


    @Test
    @DisplayName("Deve retorna o livro cadastrado")
    public void findByIdTest(){

        Book book = createNewBook("123");
        book =  entityManager.persist(book);
        Optional<Book> bookOptional = repository.findById(book.getId());

        assertThat(bookOptional.isPresent()).isTrue();
        assertThat(bookOptional.get().getTitle()).isEqualTo("Aventuras");
        assertThat(bookOptional.get().getAuthor()).isEqualTo("Fulano");
        assertThat(bookOptional.get().getIsbn()).isEqualTo("123");
    }

    private Book createNewBook(String isbn) {
        return Book.builder()
                .isbn(isbn)
                .title("Aventuras")
                .author("Fulano")
                .build();
    }

    @Test
    @DisplayName("Deve deletar o livro cadastrado")
    public void deleteBookTest(){

        Book book = createNewBook("123");

        entityManager.persist(book);
        assertThat(repository.findById(book.getId()).isPresent()).isTrue();

        repository.delete(book);
        assertThat(repository.findById(book.getId()).isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve salvar o livro")
    public void saveBookTest() {
        Book book = createNewBook("126");

        Book savedBook = repository.save(book);

        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve atualizar o livro")
    public void updatedBookTest() {
        Book book = createNewBook("126");

        Book savedBook = repository.save(book);
        savedBook.setTitle("Outro Titulo");
        repository.save(savedBook);

        Optional<Book> bookOptional = repository.findById(savedBook.getId());

        assertThat(bookOptional.isPresent()).isTrue();
        assertThat(bookOptional.get().getTitle()).isEqualTo("Outro Titulo");
    }

}
