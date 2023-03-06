package com.example.piramidadjii.bankAccountModule.repositories;

import com.example.piramidadjii.bankAccountModule.entities.Bank;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    List<Bank> findAllByIdAndTransactionDateBetween(Long id, LocalDate localDate, LocalDate now);
}
