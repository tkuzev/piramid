package com.example.piramidadjii.registrationTreeModule.repositories;

import com.example.piramidadjii.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Transaction findByPersonId(Long personId);
}
