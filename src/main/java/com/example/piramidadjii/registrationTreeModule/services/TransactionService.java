package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

import java.math.BigDecimal;
import java.util.Map;


public interface TransactionService {
    void createTransaction(RegistrationPerson registrationPerson, BigDecimal price);

    Map<SubscriptionPlan, BigDecimal> monthlyIncome(RegistrationPerson registrationPerson);
}
