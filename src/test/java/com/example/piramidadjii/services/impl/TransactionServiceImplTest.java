package com.example.piramidadjii.services.impl;

import com.example.piramidadjii.entities.Person;
import com.example.piramidadjii.entities.Transaction;
import com.example.piramidadjii.repositories.PersonRepository;
import com.example.piramidadjii.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.repositories.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionServiceImplTest {
    private List<Person> personList = new ArrayList<>();

    private List<Transaction> transactionList = new ArrayList<>();

    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    TransactionServiceImpl transactionService;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @AfterEach
    void tearDown() {
        personRepository.deleteAll(personList);

        transactionRepository.deleteAll(transactionList);
    }

    @Test
    void createTransactionWithSixPeople() {

        Person person1 = createPerson("1", 1L);
        personList.add(person1);

        Person person2 = createPerson( "2", person1.getId());
        personList.add(person2);

        Person person3 = createPerson( "3", person2.getId());
        personList.add(person3);

        Person person4 = createPerson( "4", person3.getId());
        personList.add(person4);

        Person person5 = createPerson( "5", person4.getId());
        personList.add(person5);

        Person person6 = createPerson( "person6", person5.getId());
        personList.add(person6);

        int before = transactionRepository.findAll().size();

        transactionService.createTransaction(person6, BigDecimal.valueOf(20), 0);

        int after = transactionRepository.findAll().size();

        transactionList = transactionRepository.findAll().subList(before, after);

        assertEquals(before + 5, after);
    }

    @Test
    void createTransactionWithOnePerson() {
        Person person = createPerson( "person", 1L);
        personList.add(person);

        int before = transactionRepository.findAll().size();

        transactionService.createTransaction(person, BigDecimal.valueOf(20), 0);

        int after = transactionRepository.findAll().size();

        transactionList = transactionRepository.findAll().subList(before, after);

        assertEquals(before + 1, after);
    }


    private Person createPerson(String name, long parentId) {
        Person person1 = new Person();
        person1.setSubscriptionPlan(subscriptionPlanRepository.getPlanById(1L).orElseThrow());
        person1.setName(name);
        person1.setBalance(BigDecimal.ZERO);
        person1.setParent(personRepository.getPersonById(parentId).orElseThrow());

        person1 = personRepository.save(person1);
        return person1;
    }

}