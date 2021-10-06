package com.criativoweb.libraryapi.api.service;

import com.criativoweb.libraryapi.api.model.entity.Loan;
import com.criativoweb.libraryapi.api.repository.LoanRespository;

public class LoanServiceImpl implements LoanService {
    private LoanRespository repository;

    public LoanServiceImpl(LoanRespository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        return this.repository.save(loan);
    }
}
