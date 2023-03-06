package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationPersonService;
import com.example.piramidadjii.registrationTreeModule.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    TransactionRepository transactionRepository;
    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    RegistrationPersonService registrationPersonService;

    @Test
    void createTransactionWithSixPeople() {
//        List<RegistrationPerson> streamList = createRegistrationTree(6);

        int before = transactionRepository.findAll().size();


        RegistrationPerson pesho = registrationPersonService.registerPerson("Pesho", "peshe123@abv.bg", BigDecimal.valueOf(500), 1L);
        RegistrationPerson gosho = registrationPersonService.registerPerson("Gosho", "goshe@abv.bg", BigDecimal.valueOf(500), 2L);
        RegistrationPerson mosho = registrationPersonService.registerPerson("Mosho", "Mosho@abv.bg", BigDecimal.valueOf(1500), 3L);
        RegistrationPerson tosho = registrationPersonService.registerPerson("Tosho", "Tosho@abv.bg", BigDecimal.valueOf(500), 4L);
        RegistrationPerson fosho = registrationPersonService.registerPerson("Fosho", "Fosho@abv.bg", BigDecimal.valueOf(500), 5L);
        RegistrationPerson registrationPerson = registrationPersonService.registerPerson("7sluchainidumi", "7sluchainidumi@abv.bg", BigDecimal.valueOf(500), 6L);

        transactionService.createTransaction(tosho,BigDecimal.valueOf(5000));
        transactionService.createTransaction(tosho,BigDecimal.valueOf(5000));
        transactionService.createTransaction(tosho,BigDecimal.valueOf(5000));
        transactionService.createTransaction(fosho,BigDecimal.valueOf(5000));
        transactionService.createTransaction(registrationPerson,BigDecimal.valueOf(10000));
        transactionService.createTransaction(tosho,BigDecimal.valueOf(5000));
        Map<SubscriptionPlan, BigDecimal> subscriptionPlanBigDecimalMap1 = transactionService.monthlyIncome(mosho);

//        transactionService.createTransaction(streamList.get(streamList.size() - 1), BigDecimal.valueOf(5000));
//        transactionService.createTransaction(streamList.get(streamList.size() - 1), BigDecimal.valueOf(5000));
//        transactionService.createTransaction(streamList.get(streamList.size() - 1), BigDecimal.valueOf(5000));

//        int after = transactionRepository.findAll().size();
//
//        assertEquals(before + 6, after);
//        assertEquals(BigDecimal.valueOf(250).setScale(2), transactionRepository.findByRegistrationPerson(streamList.get(5)).get().getPrice());
//
//        assertEquals("SOLD",transactionRepository.findByRegistrationPerson(streamList.get(streamList.size()-1)).get().getOperationType().toString());
//        assertEquals("BONUS",transactionRepository.findByRegistrationPerson(streamList.get(2)).get().getOperationType().toString());

        System.out.println();

    }

    @Test
    void createTransactionWithOnePerson() {
        RegistrationPerson registrationPerson = createPerson();

        int before = transactionRepository.findAll().size();

        transactionService.createTransaction(registrationPerson, BigDecimal.valueOf(5000));

        int after = transactionRepository.findAll().size();

        assertEquals(before + 2, after);
        assertEquals(5, transactionRepository.findByRegistrationPerson(registrationPerson).get().getPercent()); // expected 5%
        assertEquals(BigDecimal.valueOf(250).setScale(2), transactionRepository.findByRegistrationPerson(registrationPerson).get().getPrice());
        assertEquals("SOLD",transactionRepository.findByRegistrationPerson(registrationPerson).get().getDescription().toString());
    }

    private RegistrationPerson createPerson() {
        RegistrationPerson registrationPerson = new RegistrationPerson();
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setEmail("baiHui@bank.com");
        newBankAccount.setBalance(BigDecimal.valueOf(100.0974728383));
        bankAccountRepository.save(newBankAccount);
        registrationPerson.setName("kircho");
        registrationPerson.setBankAccount(newBankAccount);
        registrationPerson.setEmail("baiHui@bank.com");
        registrationPerson.getBankAccount().setEmail("baiHui@bank.com");
        //registrationPerson.getBankAccount().setBalance(BigDecimal.valueOf(10000));
        registrationPerson.setParent(registrationPersonRepository.getRegistrationPersonById(1L).orElseThrow());
        registrationPerson.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(1L).orElseThrow());
        registrationPerson.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
        registrationPerson = registrationPersonRepository.save(registrationPerson);
        bankAccountRepository.save(newBankAccount);
        return registrationPerson;
    }

    private List<RegistrationPerson> createRegistrationTree(int numOfNodes) {
        int n = numOfNodes + 2;

        return IntStream.range(2, n)
                .mapToObj(i -> {
                    RegistrationPerson registrationPerson = new RegistrationPerson();
                    BankAccount newBankAccount = new BankAccount();
                    newBankAccount.setEmail(i+"baiHui@bank.com");
                    newBankAccount.setBalance(BigDecimal.valueOf(300));
                    bankAccountRepository.save(newBankAccount);
                    registrationPerson.setName(i + "sluchainidumi");
                    registrationPerson.setBankAccount(newBankAccount);
                    registrationPerson.setEmail(newBankAccount.getEmail());
                    registrationPerson = registrationPersonRepository.save(registrationPerson);
                    registrationPerson.setParent(registrationPersonRepository.getRegistrationPersonById(registrationPerson.getId() - 1).orElseThrow()); // TODO random parent (between 1 and existing num of nodes)
                    registrationPerson.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(ThreadLocalRandom.current().nextLong(1, 4)).orElseThrow());
                    registrationPerson.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
                    registrationPerson = registrationPersonRepository.save(registrationPerson);
                    return registrationPerson;
                }).collect(Collectors.toList());
    }

    @Test
    void testIncome(){

    }
}