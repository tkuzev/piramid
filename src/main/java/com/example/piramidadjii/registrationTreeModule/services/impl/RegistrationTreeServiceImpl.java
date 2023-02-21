package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationTreeRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationTreeService;
import com.example.piramidadjii.registrationTreeModule.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RegistrationTreeServiceImpl implements RegistrationTreeService {
    @Autowired
    private RegistrationTreeRepository registrationTreeRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    List<SubscriptionPlan> subscriptionPlans = new ArrayList<>();


    @Override
    public void registerPerson(RegistrationTree registrationTree) {

        subscriptionPlans.addAll(subscriptionPlanRepository.findAll());
        Collections.reverse(subscriptionPlans);

        for (SubscriptionPlan subscriptionPlan : subscriptionPlans) {
            if (checkBalance(registrationTree, subscriptionPlan.getId()) > 0) {
                setSubscription(registrationTree, subscriptionPlan.getId());
                break;
            } else if (subscriptionPlan.getId() == 1) {
                throw new RuntimeException();
            }
        }

        this.registrationTreeRepository.save(registrationTree);
    }
    @Override
    public void chooseSubsPlan(RegistrationTree person, Long id){
        setSubscription(person, id);
    }

    @Override
    public void sell(BigDecimal sellPrice, RegistrationTree registrationTree) {
        this.transactionService.createTransaction(registrationTree, sellPrice);
    }

    @Override
    public void initialFee(RegistrationTree registrationTree) {

    }

    //Helper methods
    private int checkBalance(RegistrationTree registrationTree, long planId) {
        return registrationTree.getBalance().compareTo(subscriptionPlanRepository.getSubscriptionPlanById(planId).orElseThrow().getRegistrationFee());
    }

    private void setSubscription(RegistrationTree registrationTree, long id) {
        registrationTree.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow());
        BigDecimal balance = registrationTree.getBalance();
        BigDecimal fee = subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow().getRegistrationFee();
        BigDecimal newBalance = balance.subtract(fee);
        registrationTree.setBalance(newBalance);
    }

}
