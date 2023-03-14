package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.bankAccountModule.repositories.BankRepository;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationPersonService;
import com.example.piramidadjii.registrationTreeModule.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BankServiceImplTest {
    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    RegistrationPersonRepository registrationPersonRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    BankRepository bankRepository;

    @Autowired
    RegistrationPersonService registrationPersonService;

    @Test
    void testTransactionWithSevenLevelsExpectedFiveCTDT() {
        registrationPersonService.registerPerson("Person2","ebavam ti putkata maina" ,BigDecimal.valueOf(500), 1L);
        registrationPersonService.registerPerson("Person3","ebavam ti putkata maina" ,BigDecimal.valueOf(500), 2L);
        registrationPersonService.registerPerson("Person4","ebavam ti putkata maina" ,BigDecimal.valueOf(1500), 3L);
        registrationPersonService.registerPerson("Person5","ebavam ti putkata maina" ,BigDecimal.valueOf(500), 4L);
        registrationPersonService.registerPerson("Person6","ebavam ti putkata maina" ,BigDecimal.valueOf(500), 5L);
        RegistrationPerson person7 = registrationPersonService.registerPerson("Person7","pedal" ,BigDecimal.valueOf(500), 6L);

        int before = bankRepository.findAll().size();

        transactionService.createTransaction(person7,BigDecimal.valueOf(5000));

        int after = bankRepository.findAll().size();

        assertEquals(before + 6*2, after);
    }

    @Test
    void testTransactionWithOnePersonExpectedTwoCTDT() {
        RegistrationPerson person = registrationPersonService.registerPerson("Person2", "kur",BigDecimal.valueOf(500), 1L);

        int before = bankRepository.findAll().size();

        transactionService.createTransaction(person, BigDecimal.valueOf(5000));

        int after = bankRepository.findAll().size();

        assertEquals(before + 2*2, after);
    }

    //TODO: test transaction details
    //TODO: test dali izkarcha otchet za meseca??
}