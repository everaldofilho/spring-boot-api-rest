package com.criativoweb.libraryapi.api.service;

import com.criativoweb.libraryapi.api.dto.BookDTO;
import com.criativoweb.libraryapi.api.model.entity.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book any);

    Optional<Book> getById(Long id);

    void delete(Book book);

    Book update(Book book);
}
