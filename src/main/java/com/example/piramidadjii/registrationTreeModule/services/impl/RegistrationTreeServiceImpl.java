package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationTreeRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RegistrationTreeServiceImpl implements RegistrationTreeService {
    @Autowired
    private RegistrationTreeRepository registrationTreeRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    List<SubscriptionPlan> subscriptionPlans = new ArrayList<>();

    @Override
    public void registerPerson(String name, String email, BigDecimal balance, Long parentId) {
        subscriptionPlans.addAll(subscriptionPlanRepository.findAll());
        Collections.reverse(subscriptionPlans);
        RegistrationTree registrationTree = setPersonDetails(name, email, balance, parentId);

        for (SubscriptionPlan subscriptionPlan : subscriptionPlans) {
            if (checkBalance(balance, subscriptionPlan.getId()) >= 0) {
                setSubscription(registrationTree, subscriptionPlan.getId());
                break;
            } else if (subscriptionPlan.getId() == 1) {
                throw new RuntimeException();
            }
        }

        this.registrationTreeRepository.save(registrationTree);
    }

    //Helper methods
    private int checkBalance(BigDecimal balance, long planId) {
        return balance.compareTo(subscriptionPlanRepository.getSubscriptionPlanById(planId).orElseThrow().getRegistrationFee());
    }

    @Override
    public void setSubscription(RegistrationTree registrationTree, long id) {
        registrationTree.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow());
        BigDecimal balance = registrationTree.getBalance();
        BigDecimal fee = subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow().getRegistrationFee();
        BigDecimal newBalance = balance.subtract(fee);
        registrationTree.setBalance(newBalance);
    }

    private RegistrationTree setPersonDetails(String name, String email, BigDecimal balance, Long parentId) {
        RegistrationTree registrationTree = new RegistrationTree();
        registrationTree.setName(name);
        registrationTree.setEmail(email);
        registrationTree.setBalance(balance);

        registrationTree.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
        registrationTree.setParent(registrationTreeRepository.findById(parentId).orElseThrow());

        return registrationTree;
    }
}
