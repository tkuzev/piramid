package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.repositories.BankRepository;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationPersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegistrationPersonServiceImplTest {
    @Autowired
    private RegistrationPersonService registrationPersonService;

    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private BankRepository bankRepository;

    @Test()
    void testRegistrationFail(){
        assertThrows(RuntimeException.class, ()->{
            RegistrationPerson person = registrationPersonService.registerPerson("KKKKKKKKKUUUUUUURRRRRRRRRRR", new BigDecimal("100"), 1L);
        });
    }

    @Test
    void registerTestFourthTier() {
        RegistrationPerson person = registrationPersonService.registerPerson("Person", new BigDecimal("500"), 1L);
        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();

        //subscription plan
        assertEquals(4L, personFromRepo.getSubscriptionPlan().getId());

        //check money
        assertEquals(BigDecimal.valueOf(0).setScale(2), personFromRepo.getBankAccount().getBalance());
    }

    @Test
    void registerTestThirdTier() {
        RegistrationPerson person = registrationPersonService.registerPerson("Person", new BigDecimal("450"), 1L);
        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();

        //subscription plan
        assertEquals(3L, personFromRepo.getSubscriptionPlan().getId());

        //check money
        assertEquals(BigDecimal.valueOf(50).setScale(2), personFromRepo.getBankAccount().getBalance());
    }

    @Test
    void registerTestSecondTier() {
        RegistrationPerson person = registrationPersonService.registerPerson("Person", new BigDecimal("350"), 1L);
        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();

        //subscription plan
        assertEquals(2L, personFromRepo.getSubscriptionPlan().getId());

        //check money
        assertEquals(BigDecimal.valueOf(50).setScale(2), personFromRepo.getBankAccount().getBalance());

    }

    @Test
    void registerTestFirstTier() {
        RegistrationPerson person = registrationPersonService.registerPerson("Person", new BigDecimal("250"), 1L);
        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();

        //subscription plan
        assertEquals(1L, personFromRepo.getSubscriptionPlan().getId());

        //check money
        assertEquals(BigDecimal.valueOf(50).setScale(2), personFromRepo.getBankAccount().getBalance());
    }

    @Test
    void upgradeSubscriptionPlanTestSuccessfully(){
        int oldTransactions = bankRepository.findAll().size();
        RegistrationPerson person = registrationPersonService.registerPerson("KUR", new BigDecimal("250"), 1L);
        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();

        personFromRepo.getBankAccount().setBalance(personFromRepo.getBankAccount().getBalance().add(BigDecimal.valueOf(500)));
        registrationPersonService.upgradeSubscriptionPlan(personFromRepo, subscriptionPlanRepository.getSubscriptionPlanById(3L).orElseThrow());
        registrationPersonRepository.save(personFromRepo);

        assertEquals(3L, personFromRepo.getSubscriptionPlan().getId());
        assertEquals(BigDecimal.valueOf(150).setScale(2), personFromRepo.getBankAccount().getBalance());
        assertEquals(oldTransactions + 4, bankRepository.findAll().size());
    }

    @Test
    void testParentAndBankAccount() {
        RegistrationPerson person = registrationPersonService.registerPerson("Person", new BigDecimal("250"), 1L);
        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();

        //check bank account id = person id
        assertEquals(person.getId(), personFromRepo.getBankAccount().getId());

        //check parent id
        assertEquals(1L, personFromRepo.getParent().getId());
    }

    @Test
    void testRegistrationFeeTransactions() {
        RegistrationPerson person = registrationPersonService.registerPerson("Person", new BigDecimal("250"), 1L);
        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();



    }
}

