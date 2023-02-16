package com.example.piramidadjii.services.impl;

import com.example.piramidadjii.entities.SubscriptionPlan;
import com.example.piramidadjii.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.services.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

import java.util.List;

@Service
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

    public List<Long> mapFromStringToLong(String percents){

        List<Long> list = new ArrayList<>();

        String[] stringArray = percents.split("//");
        for (String string : stringArray) {
            Long longInt = Long.valueOf(string);
            list.add(longInt);
        }

        return list;
    }
}
