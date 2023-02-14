package com.example.piramidadjii.services.impl;

import com.example.piramidadjii.entities.Person;
import com.example.piramidadjii.entities.Plan;
import com.example.piramidadjii.entities.Transaction;
import com.example.piramidadjii.repositories.PersonRepository;
import com.example.piramidadjii.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.services.RegistrationTreeService;
import com.example.piramidadjii.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class RegistrationTreeServiceImpl implements RegistrationTreeService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    private int counter = 0;

    @Override
    public void registerPerson(Person person, Plan plan) {
        person.setSubscriptionPlan(plan);
        this.personRepository.save(person);
        initialFee(person, person.getSubscriptionPlan().getRegistrationFee());
    }

    @Override
    public void sell(BigDecimal sellPrice, Person person) {
        if (counter < 4 && person.getParent().getId() != 1) {

        }


    }

    @Override
    public void initialFee(Person person, BigDecimal tax) {
//        transactionService.createTransaction(person, tax,4); TODO;
    }


}
