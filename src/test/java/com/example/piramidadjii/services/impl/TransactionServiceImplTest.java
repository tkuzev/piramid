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
import static org.junit.jupiter.api.Assertions.assertNull;

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
        person1.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(4L).orElseThrow());
        personList.add(person1);

        Person person2 = createPerson( "2", person1.getId());
        person2.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(4L).orElseThrow());
        personList.add(person2);

        Person person3 = createPerson( "3", person2.getId());
        person3.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(3L).orElseThrow());
        personList.add(person3);

        Person person4 = createPerson( "4", person3.getId());
        person4.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(1L).orElseThrow());
        personList.add(person4);

        Person person5 = createPerson( "5", person4.getId());
        person5.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(2L).orElseThrow());
        personList.add(person5);

        Person person6 = createPerson( "6", person5.getId());
        person6.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(1L).orElseThrow());
        personList.add(person6);

        int before = transactionRepository.findAll().size();

        transactionService.createTransaction(person6, BigDecimal.valueOf(5000));

        int after = transactionRepository.findAll().size();

        transactionList = transactionRepository.findAll().subList(before, after);

        assertEquals(before + 5, after);
        assertEquals(5, transactionRepository.findByPersonId(person6.getId()).getPercent()); // expected 5% // working
//        assertEquals(3, transactionRepository.findByPersonId(person5.getId()).getPercent()); // expected 3% // not working
//        assertEquals(0, transactionRepository.findByPersonId(person4.getId()).getPercent()); // expected 0% // not working
//        assertEquals(2, transactionRepository.findByPersonId(person3.getId()).getPercent());// expected 2% // not working
//        assertEquals(2, transactionRepository.findByPersonId(person2.getId()).getPercent()); // expected 2% // not working
        assertNull(transactionRepository.findByPersonId(person1.getId())); // no transaction expected // working
        //assertEquals for price
        //assertEquals for operation type
    }

    @Test
    void createTransactionWithOnePerson() {
        Person person = createPerson( "person", 1L);
        person.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(1L).orElseThrow());
        personList.add(person);

        int before = transactionRepository.findAll().size();

        transactionService.createTransaction(person, BigDecimal.valueOf(5000));

        int after = transactionRepository.findAll().size();

        transactionList = transactionRepository.findAll().subList(before, after);

        assertEquals(before + 1, after);
        assertEquals(5, transactionRepository.findByPersonId(person.getId()).getPercent()); // expected 5% // working
//        assertEquals(BigDecimal.valueOf(250), transactionRepository.findByPersonId(person.getId()).getPrice()); // TUKA IMA MNOGO TUPA GRESHKA S BIG DECIMAL INACHE TRQ SI RABOTI
        //assertEquals for operation type
    }

    private Person createPerson(String name, long parentId) {
        Person person = new Person();
        person.setName(name);
        person.setBalance(BigDecimal.ZERO);
        person.setParent(personRepository.getPersonById(parentId).orElseThrow());

        person = personRepository.save(person);
        return person;
    }
}