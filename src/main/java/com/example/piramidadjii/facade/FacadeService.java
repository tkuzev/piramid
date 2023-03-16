package com.example.piramidadjii.facade;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

import java.math.BigDecimal;
import java.util.Map;

public interface FacadeService {
    void registerPerson(String name,String email,BigDecimal money, Long parentId, Long personToPutItOnId, boolean preferredDirection, SubscriptionPlan subscriptionPlan);
    void registerPerson(String name, String email, BigDecimal money, Long parentId, SubscriptionPlan subscriptionPlan);
    Map<SubscriptionPlan, BigDecimal> monthlyIncome(Long id);

    void deposit(RegistrationPerson person, BigDecimal money);

    void withdraw(RegistrationPerson person, BigDecimal money);

    void upgradeSubscriptionPlan(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan);

    void createTransaction(RegistrationPerson registrationPerson, BigDecimal price);
}
