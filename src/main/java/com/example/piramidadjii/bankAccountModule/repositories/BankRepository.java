package com.example.piramidadjii.bankAccountModule.repositories;

import com.example.piramidadjii.bankAccountModule.entities.Bank;
import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    List<Bank> findAllByIdAndTransactionDateBetween(Long id, LocalDateTime localDate, LocalDateTime now);
    Optional<Bank> findFirstByDstAccId(BankAccount bankAccount);

}
