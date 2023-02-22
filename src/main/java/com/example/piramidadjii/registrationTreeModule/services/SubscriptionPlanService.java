package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface SubscriptionPlanService {
    void createSubscriptionPlan(SubscriptionPlan subscriptionPlan);

}
