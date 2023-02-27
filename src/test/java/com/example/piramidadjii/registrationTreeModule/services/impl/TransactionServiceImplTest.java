package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.baseEntities.Transaction;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationTreeRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.TransactionRepository;
import com.example.piramidadjii.registrationTreeModule.services.TransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionServiceImplTest {
    private List<Transaction> transactionList = new ArrayList<>();
    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    RegistrationTreeRepository registrationTreeRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    BankAccountRepository bankAccountRepository;

//    @AfterEach
//    void tearDown() {
////        registrationTreeRepository.deleteAll(registrationTreeListToDelete);
//        transactionRepository.deleteAll(transactionList);
//    }

    @Test
    void createTransactionWithSixPeople() {
        List<RegistrationTree> streamList = createRegistrationTree(6);

        int before = transactionRepository.findAll().size();

        transactionService.createTransaction(streamList.get(streamList.size() - 1), BigDecimal.valueOf(5000));

        int after = transactionRepository.findAll().size();

        transactionList = transactionRepository.findAll().subList(before, after);

        assertEquals(before + 6, after);
        assertEquals(BigDecimal.valueOf(250).setScale(2), transactionRepository.findByRegistrationTree(streamList.get(5)).get().getPrice());

        assertEquals("SOLD",transactionRepository.findByRegistrationTree(streamList.get(streamList.size()-1)).get().getOperationType().toString());
        assertEquals("BONUS",transactionRepository.findByRegistrationTree(streamList.get(2)).get().getOperationType().toString());
    }

    @Test
    void createTransactionWithOnePerson() {
        RegistrationTree registrationTree = createPerson();

        int before = transactionRepository.findAll().size();

        transactionService.createTransaction(registrationTree, BigDecimal.valueOf(5000));

        int after = transactionRepository.findAll().size();

        transactionList = transactionRepository.findAll().subList(before, after);

        assertEquals(before + 2, after);
        assertEquals(5, transactionRepository.findByRegistrationTree(registrationTree).get().getPercent()); // expected 5%
        assertEquals(BigDecimal.valueOf(250).setScale(2), transactionRepository.findByRegistrationTree(registrationTree).get().getPrice());
        assertEquals("SOLD",transactionRepository.findByRegistrationTree(registrationTree).get().getOperationType().toString());
    }

    private RegistrationTree createPerson() {
        RegistrationTree registrationTree = new RegistrationTree();
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setEmail("baiHui@bank.com");
        bankAccountRepository.save(newBankAccount);
        registrationTree.setBankAccount(newBankAccount);
        registrationTree.setEmail("baiHui@bank.com");
        registrationTree.getBankAccount().setEmail("baiHui@bank.com");
        registrationTree.getBankAccount().setBalance(BigDecimal.ZERO);
        registrationTree.setParent(registrationTreeRepository.getRegistrationTreeById(1L).orElseThrow());
        registrationTree.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(1L).orElseThrow());
        registrationTree.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
        registrationTree = registrationTreeRepository.save(registrationTree);
        bankAccountRepository.save(newBankAccount);
        return registrationTree;
    }

    private List<RegistrationTree> createRegistrationTree(int numOfNodes) {
        int n = numOfNodes + 2;

        return IntStream.range(2, n)
                .mapToObj(i -> {
                    RegistrationTree registrationTree = new RegistrationTree();
                    BankAccount newBankAccount = new BankAccount();
                    newBankAccount.setEmail("baiHui@bank.com");
                    newBankAccount.setBalance(BigDecimal.valueOf(300));
                    bankAccountRepository.save(newBankAccount);
                    registrationTree.setName(String.valueOf(i));
                    registrationTree.setBankAccount(newBankAccount);
                    registrationTree.getBankAccount().setBalance(BigDecimal.ZERO);
                    registrationTree = registrationTreeRepository.save(registrationTree);
                    registrationTree.setParent(registrationTreeRepository.getRegistrationTreeById(registrationTree.getId() - 1).orElseThrow()); // TODO random parent (between 1 and existing num of nodes)
                    registrationTree.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(ThreadLocalRandom.current().nextLong(1, 4)).orElseThrow());
                    registrationTree.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
                    registrationTree = registrationTreeRepository.save(registrationTree);
                    return registrationTree;
                }).collect(Collectors.toList());
    }
}