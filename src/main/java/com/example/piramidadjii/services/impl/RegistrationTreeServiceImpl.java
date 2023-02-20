package com.example.piramidadjii.services.impl;

import com.example.piramidadjii.entities.Person;
import com.example.piramidadjii.entities.SubscriptionPlan;
import com.example.piramidadjii.enums.OperationType;
import com.example.piramidadjii.repositories.PersonRepository;
import com.example.piramidadjii.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.services.RegistrationTreeService;
import com.example.piramidadjii.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RegistrationTreeServiceImpl implements RegistrationTreeService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    List<SubscriptionPlan> subscriptionPlans = new ArrayList<>();


    @Override
    public void registerPerson(Person person) {

        subscriptionPlans.addAll(subscriptionPlanRepository.findAll());
        Collections.reverse(subscriptionPlans);

        for (SubscriptionPlan subscriptionPlan : subscriptionPlans) {
            if (checkBalance(person, subscriptionPlan.getId()) > 0) {
                setSubscription(person, subscriptionPlan.getId());
                break;
            } else if(subscriptionPlan.getId() == 1){
                throw new RuntimeException();
            }
        }

        this.personRepository.save(person);
    }

    @Override
    public void sell(BigDecimal sellPrice, Person person) {
        this.transactionService.createTransaction(person, sellPrice);
    }

    @Override
    public void initialFee(Person person) {

    }

    //Helper methods
    private int checkBalance(Person person, long planId) {
        return person.getBalance().compareTo(subscriptionPlanRepository.getSubscriptionPlanById(planId).orElseThrow().getRegistrationFee());
    }

    private void setSubscription(Person person, long id) {
        person.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow());
        BigDecimal balance=person.getBalance();
        BigDecimal fee=subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow().getRegistrationFee();
        BigDecimal newBalance=balance.subtract(fee);
        person.setBalance(newBalance);
    }
}
