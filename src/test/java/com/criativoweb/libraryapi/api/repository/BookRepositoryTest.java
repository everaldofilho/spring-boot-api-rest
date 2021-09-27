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
        Book book = Book.builder()
                .isbn(isbn)
                .title("Aventuras")
                .author("Fulano")
                .build();
        entityManager.persist(book);
        boolean exists = repository.existsByIsbn(isbn);

        Assertions.assertThat(exists).isTrue();
    }


    @Test
    @DisplayName("Deve retorna o livro cadastrado")
    public void findByIdTest(){

        Book book = Book.builder()
                .isbn("123")
                .title("Aventuras")
                .author("Fulano")
                .build();
        book =  entityManager.persist(book);
        Optional<Book> bookOptional = repository.findById(book.getId());

        Assertions.assertThat(bookOptional.isPresent()).isTrue();
        Assertions.assertThat(bookOptional.get().getTitle()).isEqualTo("Aventuras");
        Assertions.assertThat(bookOptional.get().getAuthor()).isEqualTo("Fulano");
        Assertions.assertThat(bookOptional.get().getIsbn()).isEqualTo("123");
    }

    @Test
    @DisplayName("Deve deletar o livro cadastrado")
    public void deleteBookTest(){

        Book book = Book.builder()
                .isbn("123")
                .title("Aventuras")
                .author("Fulano")
                .build();

        entityManager.persist(book);
        Assertions.assertThat(repository.findById(book.getId()).isPresent()).isTrue();

        repository.delete(book);
        Assertions.assertThat(repository.findById(book.getId()).isPresent()).isFalse();


    }


}
