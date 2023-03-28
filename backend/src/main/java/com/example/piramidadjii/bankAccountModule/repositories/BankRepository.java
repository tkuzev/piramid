package com.example.piramidadjii.bankAccountModule.repositories;

import com.example.piramidadjii.bankAccountModule.entities.Bank;
import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.baseModule.enums.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    List<Bank> findAllByDescriptionAndDstAccIdAndTransactionDateBetweenAndOperationType(Description description, BankAccount bankAccount,
                                                                                        LocalDateTime date, LocalDateTime date2, OperationType operationType);
}
