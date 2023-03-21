package com.example.piramidadjii.facade;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

import java.math.BigDecimal;
import java.util.Map;

public interface FacadeService {
    void registerPerson(RegistrationPerson registrationPerson, BigDecimal money);

    Map<SubscriptionPlan, BigDecimal> monthlyIncome(Long id);

    void deposit(Long id, BigDecimal money);

    void withdraw(Long id, BigDecimal money);

    void upgradeSubscriptionPlan(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan);

    void createTransaction(RegistrationPerson registrationPerson, BigDecimal price);
}
