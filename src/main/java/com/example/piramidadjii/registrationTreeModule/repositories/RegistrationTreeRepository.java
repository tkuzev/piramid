package com.example.piramidadjii.registrationTreeModule.repositories;

import com.example.piramidadjii.entities.Person;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationTreeRepository extends JpaRepository<RegistrationTree,Long> {

    Optional<RegistrationTree> getRegistrationTreeById(Long id);
}
