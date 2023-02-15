package com.example.piramidadjii.services.impl;

import com.example.piramidadjii.entities.Plan;
import com.example.piramidadjii.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.services.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlanServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Override
    public void createSubscriptionPlan(Plan plan) {
        Plan newPlan = new Plan();
        newPlan.setName(plan.getName());
        newPlan.setLevels(plan.getLevels());
        newPlan.setPercent(plan.getPercent());
        newPlan.setRegistrationFee(plan.getRegistrationFee());

        subscriptionPlanRepository.save(newPlan);
    }
}
