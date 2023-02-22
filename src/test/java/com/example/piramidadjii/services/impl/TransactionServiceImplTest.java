package com.example.piramidadjii.services.impl;

import com.example.piramidadjii.baseEntities.Transaction;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationTreeRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.TransactionRepository;
import com.example.piramidadjii.registrationTreeModule.services.impl.TransactionServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
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
    TransactionServiceImpl transactionService;
    @Autowired
    RegistrationTreeRepository registrationTreeRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @AfterEach
    void tearDown() {
//        registrationTreeRepository.deleteAll(registrationTreeListToDelete);
        transactionRepository.deleteAll(transactionList);
    }

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
        registrationTree.setBalance(BigDecimal.ZERO);
        registrationTree.setRegistrationTree(registrationTreeRepository.getRegistrationTreeById(1L).orElseThrow());
        registrationTree.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(1L).orElseThrow());
        registrationTree = registrationTreeRepository.save(registrationTree);
        return registrationTree;
    }

    private List<RegistrationTree> createRegistrationTree( int numOfNodes) {
        int n = numOfNodes + 2;

        List<RegistrationTree> streamList = IntStream.range(2, n)
                .mapToObj(i -> {
                    RegistrationTree registrationTree = new RegistrationTree();
                    registrationTree.setName(String.valueOf(i));
                    registrationTree.setBalance(BigDecimal.ZERO);
                    registrationTree = registrationTreeRepository.save(registrationTree);
                    registrationTree.setRegistrationTree(registrationTreeRepository.getRegistrationTreeById(registrationTree.getId() - 1).orElseThrow()); // TODO random parent (between 1 and existing num of nodes)
                    registrationTree.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(ThreadLocalRandom.current().nextLong(1, 4)).orElseThrow());
                    registrationTree = registrationTreeRepository.save(registrationTree);
                    return registrationTree;
                }).collect(Collectors.toList());
        return streamList;
    }
}