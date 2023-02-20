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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class TransactionServiceImplTest {
    private List<Person> personListToDelete = new ArrayList<>();
    private List<Person> personListToSave = new ArrayList<>();
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
        personRepository.deleteAll(personListToDelete);
        transactionRepository.deleteAll(transactionList);
    }

    @Test
    void createTransactionWithSixPeople() {
        List<Person> streamList = IntStream.range(2, 8)
                .mapToObj(i -> {
                    Person person = new Person();
                    person.setId((long) i);
                    person.setName(String.valueOf(i));
                    person.setBalance(BigDecimal.ZERO);
                    person.setParent(personRepository.getPersonById(person.getId()-1).orElseThrow());
                    person.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(ThreadLocalRandom.current().nextLong(1, 4)).orElseThrow());
                    person = personRepository.save(person);
                    return person;
                })
                        .toList();
        personListToDelete.addAll(streamList);


        int before = transactionRepository.findAll().size();

        transactionService.createTransaction(streamList.get(streamList.size()-1), BigDecimal.valueOf(5000));

        int after = transactionRepository.findAll().size();

        transactionList = transactionRepository.findAll().subList(before, after);

        assertEquals(before + 5, after);
        assertEquals(5, transactionRepository.findByPersonId(streamList.get(5).getId()).getPercent()); // expected 5% // working
        assertEquals(3, transactionRepository.findByPersonId(streamList.get(4).getId()).getPercent()); // expected 3% // not working
        assertEquals(0, transactionRepository.findByPersonId(streamList.get(3).getId()).getPercent()); // expected 0% // not working
        assertEquals(2, transactionRepository.findByPersonId(streamList.get(2).getId()).getPercent());// expected 2% // not working
        assertEquals(2, transactionRepository.findByPersonId(streamList.get(1).getId()).getPercent()); // expected 2% // not working
        assertNull(transactionRepository.findByPersonId(streamList.get(0).getId())); // no transaction expected // working
        assertEquals(BigDecimal.valueOf(250).setScale(2), transactionRepository.findByPersonId(streamList.get(5).getId()).getPrice());
        //assertEquals for operation type
    }

    @Test
    void createTransactionWithOnePerson() {
        Person person = createPerson( "person", 1L);
        person.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(1L).orElseThrow());
        person = personRepository.save(person);
        personListToDelete.add(person);

        int before = transactionRepository.findAll().size();

        transactionService.createTransaction(person, BigDecimal.valueOf(5000));

        int after = transactionRepository.findAll().size();

        transactionList = transactionRepository.findAll().subList(before, after);

        assertEquals(before + 1, after);
        assertEquals(5, transactionRepository.findByPersonId(person.getId()).getPercent()); // expected 5% // working
        assertEquals(BigDecimal.valueOf(250).setScale(2), transactionRepository.findByPersonId(person.getId()).getPrice());
        //assertEquals for operation type
    }

    private Person createPerson(String name, long parentId) {
        Person person = new Person();
        person.setName(name);
        person.setBalance(BigDecimal.ZERO);
        person.setParent(personRepository.getPersonById(parentId).orElseThrow());

        return person;
    }
}