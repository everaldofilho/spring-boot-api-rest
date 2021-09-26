package com.criativoweb.libraryapi.api.service;

import com.criativoweb.libraryapi.api.exception.BusinessException;
import com.criativoweb.libraryapi.api.model.entity.Book;
import com.criativoweb.libraryapi.api.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if (repository.existsByIsbn(book.getIsbn())) {
            throw  new BusinessException("Isbn já cadastrado.");
        }
        return repository.save(book);
    }
}
