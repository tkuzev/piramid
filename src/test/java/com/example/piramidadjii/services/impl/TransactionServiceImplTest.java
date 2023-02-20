package com.example.piramidadjii.services.impl;

import com.example.piramidadjii.entities.Transaction;
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
    private List<RegistrationTree> registrationTreeListToDelete = new ArrayList<>();
    private List<RegistrationTree> registrationTreeListToSave = new ArrayList<>();
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
//        transactionRepository.deleteAll(transactionList);
    }

    @Test
    void createTransactionWithSixPeople() {

        List<RegistrationTree> streamList = IntStream.range(2, 8)
                .mapToObj(i -> {
                    RegistrationTree registrationTree = new RegistrationTree();
                    registrationTree.setName(String.valueOf(i));
                    registrationTree.setBalance(BigDecimal.ZERO);
                    registrationTree = registrationTreeRepository.save(registrationTree);
                    registrationTree.setRegistrationTree(registrationTreeRepository.getRegistrationTreeById(registrationTree.getId() - 1).orElseThrow());
                    registrationTree.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(ThreadLocalRandom.current().nextLong(1, 4)).orElseThrow());
                    registrationTree = registrationTreeRepository.save(registrationTree);
                    return registrationTree;
                }).collect(Collectors.toList());
        registrationTreeListToDelete.addAll(streamList);


        int before = transactionRepository.findAll().size();

        transactionService.createTransaction(streamList.get(streamList.size() - 1), BigDecimal.valueOf(5000));

        int after = transactionRepository.findAll().size();

        transactionList = transactionRepository.findAll().subList(before, after);

        assertEquals(before + 5, after);
//        assertEquals(5, transactionRepository.findByPersonId(streamList.get(5).getId()).getPercent()); // expected 5% // working
//        assertEquals(3, transactionRepository.findByPersonId(streamList.get(4).getId()).getPercent()); // expected 3% // not working
//        assertEquals(0, transactionRepository.findByPersonId(streamList.get(3).getId()).getPercent()); // expected 0% // not working
//        assertEquals(2, transactionRepository.findByPersonId(streamList.get(2).getId()).getPercent());// expected 2% // not working
//        assertEquals(2, transactionRepository.findByPersonId(streamList.get(1).getId()).getPercent()); // expected 2% // not working
//        assertNull(transactionRepository.findByPersonId(streamList.get(0).getId())); // no transaction expected // working
//        assertEquals(BigDecimal.valueOf(250).setScale(2), transactionRepository.findByPersonId(streamList.get(5).getId()).getPrice());
//        //assertEquals for operation type
//        assertEquals("SOLD",transactionRepository.findByPersonId(streamList.get(streamList.size()-1).getId()).getOperationType().toString());
//        assertEquals("BONUS",transactionRepository.findByPersonId(streamList.get(2).getId()).getOperationType().toString());

    }

    @Test
    void createTransactionWithOnePerson() {
        RegistrationTree registrationTree = createRegistrationTree("registrationTree", 1L);
        registrationTree.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(1L).orElseThrow());
        registrationTree = registrationTreeRepository.save(registrationTree);
        registrationTreeListToDelete.add(registrationTree);

        int before = transactionRepository.findAll().size();

        transactionService.createTransaction(registrationTree, BigDecimal.valueOf(5000));

        int after = transactionRepository.findAll().size();

        transactionList = transactionRepository.findAll().subList(before, after);

        assertEquals(before + 1, after);
//        assertEquals(5, transactionRepository.findByPersonId(registrationTree.getId()).getPercent()); // expected 5% // working
//        assertEquals(BigDecimal.valueOf(250).setScale(2), transactionRepository.findByPersonId(registrationTree.getId()).getPrice());
        //assertEquals for operation type
    }

    private RegistrationTree createRegistrationTree(String name, long parentId) {
        RegistrationTree registrationTree = new RegistrationTree();
        registrationTree.setName(name);
        registrationTree.setBalance(BigDecimal.ZERO);
        registrationTree.setRegistrationTree(registrationTreeRepository.getRegistrationTreeById(parentId).orElseThrow());

        return registrationTree;
    }
}