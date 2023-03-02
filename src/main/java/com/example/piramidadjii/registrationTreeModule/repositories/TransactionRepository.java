package com.example.piramidadjii.registrationTreeModule.repositories;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTransaction;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<RegistrationTransaction, Long> {
    Optional<RegistrationTransaction> findByRegistrationPerson(RegistrationPerson registrationPerson);

    List<RegistrationTransaction> findAllByRegistrationPersonAndDateBetween(RegistrationPerson registrationPerson, LocalDate date,LocalDate date2);
}
