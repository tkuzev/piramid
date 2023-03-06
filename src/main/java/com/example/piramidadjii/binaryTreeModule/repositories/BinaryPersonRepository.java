package com.example.piramidadjii.binaryTreeModule.repositories;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BinaryPersonRepository extends JpaRepository<BinaryPerson, Long> {
    Optional<BinaryPerson> findById(Long id);
}
