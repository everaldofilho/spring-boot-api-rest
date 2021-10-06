package com.criativoweb.libraryapi.api.service;

import com.criativoweb.libraryapi.api.model.entity.Book;
import com.criativoweb.libraryapi.api.model.entity.Loan;
import com.criativoweb.libraryapi.api.repository.LoanRespository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Properties;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    @MockBean
    private LoanRespository repository;

    private LoanService service;

    @BeforeEach
    public void setUp() {
        this.service = new LoanServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um empréstimo")
    public void saveLoanTest() {
        Book book = Book.builder().id(1L).build();
        String customer = "Fulano";

        Loan savingloan = Loan.builder()
                .book(book)
                .custumer(customer)
                .loanDate(LocalDate.now())
                .build();

        Loan savedLoan = Loan.builder()
                .book(book)
                .id(2L)
                .custumer(customer)
                .loanDate(LocalDate.now())
                .build();

        Mockito.when(repository.save(savingloan)).thenReturn(savedLoan);

        Loan loan = service.save(savingloan);

        Assertions.assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        Assertions.assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        Assertions.assertThat(loan.getCustumer()).isEqualTo(savedLoan.getCustumer());
        Assertions.assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
    }
}
