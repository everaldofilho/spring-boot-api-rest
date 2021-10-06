package com.criativoweb.libraryapi.api.repository;

import com.criativoweb.libraryapi.api.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRespository extends JpaRepository<Loan, Long> {
}
