package com.example.piramidadjii.repositories;

import com.example.piramidadjii.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Transaction findByPersonId(Long personId);
}
