package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationTreeRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationTreeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RegistrationTreeServiceImplTest {
    private RegistrationTree registrationTree;

    @Autowired
    private RegistrationTreeService registrationTreeService;

    @Autowired
    private RegistrationTreeRepository registrationTreeRepository;

    @Test
    void registerTestFourthTier() {
        registrationTreeService.registerPerson("Person", "email@person.com", new BigDecimal("500"), 1L);

        RegistrationTree registrationTree = registrationTreeRepository.getFirstByEmail("email@person.com").orElseThrow();

        assertEquals(4L, (long) registrationTree.getSubscriptionPlan().getId());
    }

    @Test
    void registerTestThirdTier() {
        registrationTreeService.registerPerson("Person", "email@person2.com", new BigDecimal("450"), 1L);

        RegistrationTree registrationTree = registrationTreeRepository.getFirstByEmail("email@person2.com").orElseThrow();

        assertEquals(3L, (long) registrationTree.getSubscriptionPlan().getId());
    }

    @Test
    void registerTestSecondTier() {
        registrationTreeService.registerPerson("Person", "email@person3.com", new BigDecimal("350"), 1L);

        RegistrationTree registrationTree = registrationTreeRepository.getFirstByEmail("email@person3.com").orElseThrow();

        assertEquals(2L, (long) registrationTree.getSubscriptionPlan().getId());
    }

    @Test
    void registerTestFirstTier() {
        registrationTreeService.registerPerson("Person", "email@person4.com", new BigDecimal("250"), 1L);

        RegistrationTree registrationTree = registrationTreeRepository.getFirstByEmail("email@person4.com").orElseThrow();

        assertEquals(1L, (long) registrationTree.getSubscriptionPlan().getId());
    }
}

