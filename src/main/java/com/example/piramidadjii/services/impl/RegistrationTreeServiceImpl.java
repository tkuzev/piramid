package com.example.piramidadjii.services.impl;

import com.example.piramidadjii.entities.Person;
import com.example.piramidadjii.entities.Plan;
import com.example.piramidadjii.repositories.PersonRepository;
import com.example.piramidadjii.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.services.RegistrationTreeService;
import com.example.piramidadjii.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RegistrationTreeServiceImpl implements RegistrationTreeService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public void registerPerson(Person person) {

        if (checkBalance(person, 4L) >0){
            setSubscription(person, 4L);
        }else if (checkBalance(person, 3L) >0){
            setSubscription(person, 3L);
        }else if (checkBalance(person, 2L) >0){
            setSubscription(person, 2L);
        }else if (checkBalance(person, 1L) >0){
            setSubscription(person, 1L);
        }else {
            throw new RuntimeException();
        }

        this.personRepository.save(person);
    }


    private int checkBalance(Person person, long id) {
        return person.getBalance().compareTo(subscriptionPlanRepository.getPlanById(id).orElseThrow().getRegistrationFee());
    }

    private void setSubscription(Person person, long id) {
        person.setSubscriptionPlan(subscriptionPlanRepository.getPlanById(id).orElseThrow());
        BigDecimal balance=person.getBalance();
        BigDecimal fee=subscriptionPlanRepository.getPlanById(id).orElseThrow().getRegistrationFee();
        BigDecimal newBalance=balance.subtract(fee);
        person.setBalance(newBalance);
    }

    @Override
    public void sell(BigDecimal sellPrice, Person person) {
        this.transactionService.createTransaction(person, sellPrice, 0);
    }

    @Override
    public void initialFee(Person person) {

    }
}
