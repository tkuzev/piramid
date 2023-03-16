package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;



public interface SubscriptionPlanService {
    void createSubscriptionPlan(SubscriptionPlan subscriptionPlan);

    void upgradeSubscriptionPlan(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan);
}
