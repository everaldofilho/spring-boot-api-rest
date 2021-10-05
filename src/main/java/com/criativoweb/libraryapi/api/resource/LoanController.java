package com.criativoweb.libraryapi.api.resource;

import com.criativoweb.libraryapi.api.dto.LoanDTO;
import com.criativoweb.libraryapi.api.model.entity.Book;
import com.criativoweb.libraryapi.api.model.entity.Loan;
import com.criativoweb.libraryapi.api.service.BookService;
import com.criativoweb.libraryapi.api.service.LoanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LoanService service;

    @Autowired
    private BookService bookService;

    public LoanController(ModelMapper modelMapper, LoanService service, BookService bookService) {
        this.modelMapper = modelMapper;
        this.service = service;
        this.bookService = bookService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid LoanDTO dto) {
        Book book = bookService.getBookByIsbn(dto.getIsbn()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn")
        );

        Loan loan = Loan.builder()
                .book(book)
                .custumer(dto.getCustomer())
                .loanDate(LocalDate.now())
                .build();

        loan = service.save(loan);

        return loan.getId();
    }
}
