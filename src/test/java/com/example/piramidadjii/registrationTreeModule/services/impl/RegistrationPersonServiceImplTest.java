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
        RegistrationPerson person = registrationPersonService.registerPerson("Person", new BigDecimal("500"), 1L);


        assertEquals(4L, (long) person.getSubscriptionPlan().getId());
    }

    @Test
    void registerTestThirdTier() {
        RegistrationPerson person = registrationPersonService.registerPerson("Person", new BigDecimal("450"), 1L);


        assertEquals(3L, (long) person.getSubscriptionPlan().getId());
    }

    @Test
    void registerTestSecondTier() {
        RegistrationPerson person = registrationPersonService.registerPerson("Person", new BigDecimal("350"), 1L);

        assertEquals(2L, (long) person.getSubscriptionPlan().getId());
    }

    @Test
    void registerTestFirstTier() {
        RegistrationPerson person = registrationPersonService.registerPerson("Person", new BigDecimal("250"), 1L);


        assertEquals(1L, (long) person.getSubscriptionPlan().getId());
    }

    @Test
    void upgradePlanTest(){
        RegistrationPerson person = registrationPersonService.registerPerson("Person", new BigDecimal("250"), 1L);
        person.getBankAccount().setBalance(person.getBankAccount().getBalance().add(BigDecimal.valueOf(500L)));

        registrationPersonService.upgradeSubscriptionPlan(person,subscriptionPlanRepository.getSubscriptionPlanById(3L).orElseThrow());

        assertEquals(3L, person.getSubscriptionPlan().getId());
        assertEquals(BigDecimal.valueOf(150).setScale(2), person.getBankAccount().getBalance());
    }
}

