package com.example.piramidadjii.registrationTreeModule.repositories;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationPersonRepository extends JpaRepository<RegistrationPerson, Long> {

    Optional<RegistrationPerson> getRegistrationPersonById(Long id);


    List<RegistrationPerson> getAllBySubscriptionExpirationDate(LocalDate date);

    Optional<RegistrationPerson> findByEmail(String email);
}
