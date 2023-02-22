package com.example.piramidadjii.registrationTreeModule.repositories;

import com.example.piramidadjii.baseEntities.Transaction;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByRegistrationTree(RegistrationTree registrationTree);
}
