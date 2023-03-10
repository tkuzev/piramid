package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.entities.Bank;
import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.bankAccountModule.repositories.BankRepository;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.baseModule.enums.OperationType;
import com.example.piramidadjii.configModule.ConfigurationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationPersonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class RegistrationPersonServiceImpl implements RegistrationPersonService {
    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private ConfigurationService configurationService;

    private static final long HELPER_BANK_ACCOUNT_ID = -1;

    @Override
    @Transactional
    public RegistrationPerson registerPerson(String name, BigDecimal money, Long parentId) {
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanRepository.findAll().stream().sorted
                (Comparator.comparing(SubscriptionPlan::getRegistrationFee).reversed()).toList();
        RegistrationPerson registrationPerson = setPersonDetails(name, parentId);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(money);
        bankAccountRepository.save(bankAccount);
        registrationPerson.setBankAccount(bankAccount);
        registrationPersonRepository.save(registrationPerson);
        for (SubscriptionPlan subscriptionPlan : subscriptionPlans) {
            if (checkBalance(registrationPerson.getBankAccount().getBalance(), subscriptionPlan.getId()) >= 0) {
                setSubscription(registrationPerson, subscriptionPlan.getId());
                registrationPerson.setIsSubscriptionEnabled(true);
                registrationPersonRepository.save(registrationPerson);
                break;
            } else if (subscriptionPlan.getId() == 1) {
                throw new RuntimeException();
            }
        }
        return registrationPerson;
    }

    //Helper methods
    private int checkBalance(BigDecimal balance, long planId) {
        return balance.compareTo(subscriptionPlanRepository.getSubscriptionPlanById(planId).orElseThrow().getRegistrationFee());
    }

    @Override
    public void setSubscription(RegistrationPerson registrationPerson, long id) {
        BankAccount helperBankAccount = bankAccountRepository.findById(HELPER_BANK_ACCOUNT_ID).orElseThrow();
        BankAccount bank = bankAccountRepository.findById(registrationPerson.getId()).orElseThrow();
        registrationPerson.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow());
        BigDecimal balance = bank.getBalance();
        BigDecimal fee = subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow().getRegistrationFee();
        BigDecimal newBalance = balance.subtract(fee);
        bank.setBalance(newBalance);
        bankAccountRepository.save(bank);


        configurationService.transactionBoiler(helperBankAccount, registrationPerson, registrationPerson.
                getSubscriptionPlan(), Description.REGISTRATION_FEE);
    }


    private RegistrationPerson setPersonDetails(String name, Long parentId) {
        RegistrationPerson registrationPerson = RegistrationPerson.builder()
                .name(name)
                .subscriptionExpirationDate(LocalDate.now().plusMonths(1))
                .parent(registrationPersonRepository.findById(parentId).orElseThrow())
                .build();
        //registrationTreeRepository.save(registrationTree);
        return registrationPerson;
    }

    @Override
    public void upgradeSubscriptionPlan(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan) {
        BankAccount helperBankAccount = bankAccountRepository.findById(HELPER_BANK_ACCOUNT_ID).orElseThrow();
        if (isUpdateUnavailable(registrationPerson, subscriptionPlan)) {
            return;
        }
        registrationPerson.getBankAccount().setBalance(registrationPerson.getBankAccount().getBalance().subtract(subscriptionPlan.getRegistrationFee()));
        configurationService.transactionBoiler(helperBankAccount, registrationPerson, subscriptionPlan, Description.UPDATE_PLAN_FEE);
        registrationPerson.setSubscriptionPlan(subscriptionPlan);
    }

    private static boolean isUpdateUnavailable(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan) {
        return registrationPerson.getSubscriptionPlan().getPercents().length() > subscriptionPlan.getPercents().length()
                || registrationPerson.getBankAccount().getBalance().compareTo(subscriptionPlan.getRegistrationFee().subtract(registrationPerson.getSubscriptionPlan().getRegistrationFee())) < 0;
    }
}
