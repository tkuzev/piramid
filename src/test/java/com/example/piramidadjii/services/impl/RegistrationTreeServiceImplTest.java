package com.example.piramidadjii.services.impl;

import com.example.piramidadjii.entities.Person;
import com.example.piramidadjii.repositories.PersonRepository;
import com.example.piramidadjii.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.services.RegistrationTreeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.math.BigDecimal;

@SpringBootTest
class RegistrationTreeServiceImplTest {
    private Person person;

    @Autowired
    private RegistrationTreeService registrationTreeService;

    @Autowired
    private PersonRepository personRepository;


    @BeforeEach
    void setUp() {
        person = new Person();
    }

    @AfterEach
    void tearDown(){
        personRepository.delete(person);
    }

    @Test
    void registerTestFourthTier() {
        person.setBalance(new BigDecimal("600"));
        person = personRepository.save(person);
        registrationTreeService.registerPerson(person);

        Person savedPerson = personRepository.getPersonById(person.getId()).orElseThrow();

        Assert.isTrue(savedPerson.getSubscriptionPlan().getId().equals(4L), "registerTestFourthTier has failed");
    }
    @Test
    void registerTestThirdTier() {
        person.setBalance(new BigDecimal("450"));
        person = personRepository.save(person);
        registrationTreeService.registerPerson(person);

        Person savedPerson = personRepository.getPersonById(person.getId()).orElseThrow();

        Assert.isTrue(savedPerson.getSubscriptionPlan().getId().equals(3L), "registerTestThirdTier has failed");
    }

    @Test
    void registerTestSecondTier() {
        person.setBalance(new BigDecimal("350"));
        person = personRepository.save(person);
        registrationTreeService.registerPerson(person);

        Person savedPerson = personRepository.getPersonById(person.getId()).orElseThrow();

        Assert.isTrue(savedPerson.getSubscriptionPlan().getId().equals(2L), "registerTestSecondTier has failed");
    }

    @Test
    void registerTestFirstTier() {
        person.setBalance(new BigDecimal("250"));
        person = personRepository.save(person);
        registrationTreeService.registerPerson(person);

        Person savedPerson = personRepository.getPersonById(person.getId()).orElseThrow();

        Assert.isTrue(savedPerson.getSubscriptionPlan().getId().equals(1L), "registerTestFirstTier has failed");
    }

    //TODO check DALI RABOT RUN TIME EXEPSHAN
}

