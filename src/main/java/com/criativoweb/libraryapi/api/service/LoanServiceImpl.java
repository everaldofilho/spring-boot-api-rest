package com.criativoweb.libraryapi.api.service;

import com.criativoweb.libraryapi.api.dto.LoanFilterDTO;
import com.criativoweb.libraryapi.api.exception.BusinessException;
import com.criativoweb.libraryapi.api.model.entity.Book;
import com.criativoweb.libraryapi.api.model.entity.Loan;
import com.criativoweb.libraryapi.api.repository.LoanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {
    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if(repository.existsByBookAndReturned(loan.getBook()) == true) {
            throw new BusinessException("Book already loaned");
        }
        return repository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return repository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO filter, Pageable pageRequest) {
        return repository.findByBookIsbnOrCustomer(filter.getIsbn(), filter.getCustomer(), pageRequest);
    }

    @Override
    public Page<Loan> getLoansByBook(Book book, Pageable pageable) {
        return repository.findByBook(book, pageable);
    }

    @Override
    public List<Loan> getAllLateLoan() {
        final Integer loanDays = 4;
        LocalDate threeDate = LocalDate.now().minusDays(loanDays);

        return repository.findByLoanDateLessThanAndNotReturned(threeDate);
    }
}
