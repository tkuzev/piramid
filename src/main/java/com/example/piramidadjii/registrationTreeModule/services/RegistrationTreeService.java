package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

import java.math.BigDecimal;


public interface RegistrationTreeService {

    void registerPerson(String name, String email, BigDecimal balance, Long parentId);


    void setSubscription(RegistrationTree registrationTree, long id);
    void upgradeSubscriptionPlan(RegistrationTree registrationTree, SubscriptionPlan subscriptionPlan);
}
