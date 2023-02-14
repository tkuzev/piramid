package com.example.piramidadjii.repositories;

import com.example.piramidadjii.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {

    Optional<Person> getPersonById(Long id);
}
