package com.example.piramidadjii.configModule;

import com.example.piramidadjii.baseModule.baseEntites.Person;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

import java.math.BigDecimal;

public interface ConfigurationService {
     boolean isEligable(SubscriptionPlan subscriptionPlan);
//
//     BigDecimal monthTaxPaid(Person person);
//
//     boolean isTaxPaid(Person person);
}
