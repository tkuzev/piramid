package com.example.piramidadjii.services.impl;

import com.example.piramidadjii.entities.Person;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationTreeRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationTreeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.math.BigDecimal;

@SpringBootTest
class RegistrationTreeServiceImplTest {
    private RegistrationTree registrationTree;

    @Autowired
    private RegistrationTreeService registrationTreeService;

    @Autowired
    private RegistrationTreeRepository registrationTreeRepository;


    @BeforeEach
    void setUp() {
        registrationTree = new RegistrationTree();
    }

    @AfterEach
    void tearDown(){
        registrationTreeRepository.delete(registrationTree);
    }

    @Test
    void registerTestFourthTier() {
        registrationTree.setBalance(new BigDecimal("600"));
        registrationTree = registrationTreeRepository.save(registrationTree);
        registrationTreeService.registerPerson(registrationTree);

        Person savedPerson = registrationTreeRepository.getRegistrationTreeById(registrationTree.getId()).orElseThrow();

        Assert.isTrue(savedPerson.getSubscriptionPlan().getId().equals(4L), "registerTestFourthTier has failed");
    }
    @Test
    void registerTestThirdTier() {
        registrationTree.setBalance(new BigDecimal("450"));
        registrationTree = registrationTreeRepository.save(registrationTree);
        registrationTreeService.registerPerson(registrationTree);

        Person savedPerson = registrationTreeRepository.getRegistrationTreeById(registrationTree.getId()).orElseThrow();

        Assert.isTrue(savedPerson.getSubscriptionPlan().getId().equals(3L), "registerTestThirdTier has failed");
    }

    @Test
    void registerTestSecondTier() {
        registrationTree.setBalance(new BigDecimal("350"));
        registrationTree = registrationTreeRepository.save(registrationTree);
        registrationTreeService.registerPerson(registrationTree);

        Person savedPerson = registrationTreeRepository.getRegistrationTreeById(registrationTree.getId()).orElseThrow();

        Assert.isTrue(savedPerson.getSubscriptionPlan().getId().equals(2L), "registerTestSecondTier has failed");
    }

    @Test
    void registerTestFirstTier() {
        registrationTree.setBalance(new BigDecimal("250"));
        registrationTree = registrationTreeRepository.save(registrationTree);
        registrationTreeService.registerPerson(registrationTree);

        Person savedPerson = registrationTreeRepository.getRegistrationTreeById(registrationTree.getId()).orElseThrow();

        Assert.isTrue(savedPerson.getSubscriptionPlan().getId().equals(1L), "registerTestFirstTier has failed");
    }
}

