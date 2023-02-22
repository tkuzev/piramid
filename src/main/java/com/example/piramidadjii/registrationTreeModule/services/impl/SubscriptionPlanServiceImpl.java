package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public void createSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        SubscriptionPlan newSubscriptionPlan = new SubscriptionPlan();
        newSubscriptionPlan.setName(subscriptionPlan.getName());
        newSubscriptionPlan.setPercents(subscriptionPlan.getPercents());
        newSubscriptionPlan.setRegistrationFee(subscriptionPlan.getRegistrationFee());
        subscriptionPlanRepository.save(newSubscriptionPlan);
    }

}
