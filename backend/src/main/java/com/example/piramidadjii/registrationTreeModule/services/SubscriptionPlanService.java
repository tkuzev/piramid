package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

import java.util.List;


public interface SubscriptionPlanService {
    void createSubscriptionPlan(SubscriptionPlan subscriptionPlan);

    void upgradeSubscriptionPlan(Long id, SubscriptionPlan subscriptionPlan);

    List<SubscriptionPlan> listOfAllSubscriptionPlans();
}
