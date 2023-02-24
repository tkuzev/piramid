package com.example.piramidadjii.binaryTreeModule.repositories;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinaryTransactionRepository extends JpaRepository<BinaryTransaction,Long> {
}
