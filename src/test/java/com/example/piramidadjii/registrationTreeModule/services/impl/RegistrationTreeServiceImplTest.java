package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationTreeRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
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

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

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

    @Test
    void upgradePlanTest(){
        registrationTreeService.registerPerson("Person", "email@puhi.com", new BigDecimal("250"), 1L);
        RegistrationTree registrationTree = registrationTreeRepository.getFirstByEmail("email@puhi.com").orElseThrow();
        registrationTree.setBalance(registrationTree.getBalance().add(BigDecimal.valueOf(500L)));

        registrationTreeService.upgradeSubscriptionPlan(registrationTree,subscriptionPlanRepository.getSubscriptionPlanById(3L).orElseThrow());

        assertEquals(3L,registrationTree.getSubscriptionPlan().getId());
        assertEquals(BigDecimal.valueOf(150).setScale(2), registrationTree.getBalance());
    }
}

