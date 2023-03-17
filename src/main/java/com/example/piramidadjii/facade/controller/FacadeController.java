package com.example.piramidadjii.facade.controller;

import com.example.piramidadjii.facade.FacadeService;
import com.example.piramidadjii.facade.dto.DepositDTO;
import com.example.piramidadjii.facade.dto.RegisterPersonDTO;
import com.example.piramidadjii.facade.dto.UpgradeSubscriptionPlanDTO;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.services.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;


@RestController
public class FacadeController {

    @Autowired
    private FacadeService facadeService;

    @Autowired
    private SubscriptionPlanService subscriptionPlanService;
    @PostMapping("/register")
    public void registerPerson(@RequestBody RegisterPersonDTO registerPersonDTO){
        SubscriptionPlan subscriptionPlan = subscriptionPlanService.assignSubscriptionPlan(registerPersonDTO.getMoney());
        if (subscriptionPlan.isEligibleForBinary()){
            facadeService.registerPerson(registerPersonDTO.getName(),
                    registerPersonDTO.getEmail(),registerPersonDTO.getMoney(),
                    registerPersonDTO.getParentId(),registerPersonDTO.getPersonToPutItOn(),
                    registerPersonDTO.isPreferredDirection(),subscriptionPlan);
        }else {
            facadeService.registerPerson(registerPersonDTO.getName(),registerPersonDTO.getEmail(),registerPersonDTO.getMoney(),
                    registerPersonDTO.getParentId(),subscriptionPlan);
        }

    }

    @GetMapping("/income/{id}")
    public Map<SubscriptionPlan, BigDecimal> monthlyIncome(@PathVariable Long id){
        return facadeService.monthlyIncome(id);
    }

    @PostMapping("/user/deposit")
    public void deposit(@RequestBody DepositDTO depositDTO){
        facadeService.deposit(depositDTO.getId(),depositDTO.getMoney());
    }

    @PostMapping("/user/withdraw")
    public void withdraw(@RequestBody DepositDTO depositDTO){
        facadeService.withdraw(depositDTO.getId(),depositDTO.getMoney());
    }

    @PostMapping("/user/subscription-plan/upgrade")
    public void upgradeSubscriptionPlan(@RequestBody UpgradeSubscriptionPlanDTO upgradeSubscriptionPlanDTO){
        facadeService.
                upgradeSubscriptionPlan(upgradeSubscriptionPlanDTO.getRegistrationPerson(),
                        upgradeSubscriptionPlanDTO.getSubscriptionPlan());
    }
}
