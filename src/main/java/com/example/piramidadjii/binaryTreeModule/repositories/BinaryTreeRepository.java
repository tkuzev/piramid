package com.example.piramidadjii.binaryTreeModule.repositories;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryTree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BinaryTreeRepository extends JpaRepository<BinaryTree, Long> {
    Optional<BinaryTree> findByEmail(String email);
}
