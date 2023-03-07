package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationPersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RegistrationPersonServiceImplTest {
    @Autowired
    private RegistrationPersonService registrationPersonService;

    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Test
    void registerTestFourthTier() {
        registrationPersonService.registerPerson("Person",  new BigDecimal("500"), 1L);

        RegistrationPerson registrationPerson = registrationPersonRepository.getFirstByEmail("email@person.com").orElseThrow();

        assertEquals(4L, (long) registrationPerson.getSubscriptionPlan().getId());
    }

    @Test
    void registerTestThirdTier() {
        registrationPersonService.registerPerson("Person", new BigDecimal("450"), 1L);

        RegistrationPerson registrationPerson = registrationPersonRepository.getFirstByEmail("email@person2.com").orElseThrow();

        assertEquals(3L, (long) registrationPerson.getSubscriptionPlan().getId());
    }

    @Test
    void registerTestSecondTier() {
        registrationPersonService.registerPerson("Person", new BigDecimal("350"), 1L);

        RegistrationPerson registrationPerson = registrationPersonRepository.getFirstByEmail("email@person3.com").orElseThrow();

        assertEquals(2L, (long) registrationPerson.getSubscriptionPlan().getId());
    }

    @Test
    void registerTestFirstTier() {
        registrationPersonService.registerPerson("Person", new BigDecimal("250"), 1L);

        RegistrationPerson registrationPerson = registrationPersonRepository.getFirstByEmail("email@person4.com").orElseThrow();

        assertEquals(1L, (long) registrationPerson.getSubscriptionPlan().getId());
    }

    @Test
    void upgradePlanTest(){
        registrationPersonService.registerPerson("Person", new BigDecimal("250"), 1L);
        RegistrationPerson registrationPerson = registrationPersonRepository.getFirstByEmail("email@puhi.com").orElseThrow();
        registrationPerson.getBankAccount().setBalance(registrationPerson.getBankAccount().getBalance().add(BigDecimal.valueOf(500L)));

        registrationPersonService.upgradeSubscriptionPlan(registrationPerson,subscriptionPlanRepository.getSubscriptionPlanById(3L).orElseThrow());

        assertEquals(3L, registrationPerson.getSubscriptionPlan().getId());
        assertEquals(BigDecimal.valueOf(150).setScale(2), registrationPerson.getBankAccount().getBalance());
    }
}

