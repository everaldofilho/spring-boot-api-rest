package com.criativoweb.libraryapi.api.service;

import com.criativoweb.libraryapi.api.dto.LoanFilterDTO;
import com.criativoweb.libraryapi.api.exception.BusinessException;
import com.criativoweb.libraryapi.api.model.entity.Book;
import com.criativoweb.libraryapi.api.model.entity.Loan;
import com.criativoweb.libraryapi.api.repository.LoanRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    @MockBean
    private LoanRepository repository;

    private LoanService service;

    @BeforeEach
    public void setUp() {
        this.service = new LoanServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um empréstimo")
    public void saveLoanTest() {
        String customer = "Fulano";
        Loan savingloan = buildLoan(customer, 1L);

        Loan savedLoan = Loan.builder()
                .book(savingloan.getBook())
                .id(2L)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();

        Mockito.when(repository.existsByBookAndReturned(savingloan.getBook())).thenReturn(false);
        Mockito.when(repository.save(savingloan)).thenReturn(savedLoan);

        Loan loan = service.save(savingloan);

        Assertions.assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        Assertions.assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        Assertions.assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        Assertions.assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
    }


    @Test
    @DisplayName("Deve lançar erro de negócio ao salvar um emprestimo com livro já emprestado")
    public void loanedBookSaveTest() {

        String customer = "Fulano";

        Loan savingLoan = buildLoan(customer, 1l);

        Mockito.when(repository.existsByBookAndReturned(savingLoan.getBook())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(savingLoan));

        Assertions.assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Book already loaned");

        Mockito.verify(repository, Mockito.never()).save(savingLoan);

    }

    public static Loan buildLoan(String customer, Long bookId) {
        Book book = Book.builder().id(bookId).build();
        return Loan.builder()
                .book(book)
                .customer(customer)
                .customerEmail(customer.concat("@gmail.com"))
                .loanDate(LocalDate.now())
                .build();
    }

    @Test
    @DisplayName("Deve obter as informações de um empréstimo pelo ID")
    public void getLoanDetaisTest() {

        long id = 1l;
        Loan loan = buildLoan("Fulano", id);
        loan.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(loan));

        Optional<Loan> result = service.getById(id);

        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get().getId()).isEqualTo(id);
        Assertions.assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
        Assertions.assertThat(result.get().getBook()).isEqualTo(loan.getBook());
        Assertions.assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());

        Mockito.verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar um emprestimo")
    public void updateLoanTest() {
        Loan loan = buildLoan("Test", 1l);
        loan.setId(1l);
        loan.setReturned(true);

        Mockito.when(repository.save(loan)).thenReturn(loan);

        Loan updateLoan = service.update(loan);

        Assertions.assertThat(updateLoan.getReturned()).isTrue();
        Mockito.verify(repository).save(loan);
    }


    @Test
    @DisplayName("Deve filtrar pelas propriedades")
    public void findLoanTest() {
        LoanFilterDTO dto = LoanFilterDTO.builder().customer("fulano").isbn("321").build();
        Loan loan = buildLoan("test", 1L);

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Loan> lista = Arrays.asList(loan);
        Page<Loan> page = new PageImpl<Loan>(lista, PageRequest.of(0, 10), lista.size());
        Mockito.when(repository.findByBookIsbnOrCustomer(Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.any(PageRequest.class)))
                .thenReturn(page);

        Page<Loan> result = service.find(dto, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }
}
