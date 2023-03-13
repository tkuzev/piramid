package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import org.springframework.stereotype.Service;



public interface SubscriptionPlanService {
    void createSubscriptionPlan(SubscriptionPlan subscriptionPlan);

    void upgradeSubscriptionPlan(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan);
}
