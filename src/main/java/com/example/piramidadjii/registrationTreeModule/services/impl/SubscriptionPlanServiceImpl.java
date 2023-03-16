package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.configModule.ConfigurationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private ConfigurationService configurationService;
    private static final long BOSS_BANK_ACCOUNT_ID = 1;

    @Override
    public void createSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        SubscriptionPlan newSubscriptionPlan = SubscriptionPlan.builder()
                .name(subscriptionPlan.getName())
                .percents(subscriptionPlan.getPercents())
                .registrationFee(subscriptionPlan.getRegistrationFee())
                .build();
        subscriptionPlanRepository.save(newSubscriptionPlan);
    }

    @Override
    public void upgradeSubscriptionPlan(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan) {
        BankAccount bossBankAccount = bankAccountRepository.findById(BOSS_BANK_ACCOUNT_ID).orElseThrow();

        if (isUpdateUnavailable(registrationPerson, subscriptionPlan)) {
            return;
        }

        BigDecimal money = subscriptionPlan.getRegistrationFee().subtract(registrationPerson.getSubscriptionPlan().getRegistrationFee());

        registrationPerson.getBankAccount().setBalance(registrationPerson.getBankAccount().getBalance().subtract(money));
        bossBankAccount.setBalance(bossBankAccount.getBalance().add(money));
        configurationService.transactionBoiler(bossBankAccount, registrationPerson, subscriptionPlan, Description.UPDATE_PLAN_FEE, money);
        registrationPerson.setSubscriptionPlan(subscriptionPlan);
        bankAccountRepository.save(registrationPerson.getBankAccount());
        bankAccountRepository.save(bossBankAccount);
    }

    @Override
    public SubscriptionPlan assignSubscriptionPlan(BigDecimal money) {
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanRepository.findAll().stream().sorted
                (Comparator.comparing(SubscriptionPlan::getRegistrationFee).reversed()).toList();

        return subscriptionPlans.stream()
                .filter(x -> checkBalance(money, x.getId()) >= 0)
                .findFirst().orElseThrow();
    }

    private static boolean isUpdateUnavailable(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan) {
        return registrationPerson.getSubscriptionPlan().getPercents().length() > subscriptionPlan.getPercents().length()
                || registrationPerson.getBankAccount().getBalance().compareTo(subscriptionPlan.getRegistrationFee().subtract(registrationPerson.getSubscriptionPlan().getRegistrationFee())) < 0;
    }

    private int checkBalance(BigDecimal balance, long planId) {
        return balance.compareTo(subscriptionPlanRepository.getSubscriptionPlanById(planId).orElseThrow().getRegistrationFee());
    }
}
