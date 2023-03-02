package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RegistrationPersonServiceImpl implements RegistrationPersonService {
    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    List<SubscriptionPlan> subscriptionPlans = new ArrayList<>();


    @Override
    public RegistrationPerson registerPerson(String name, String email, BigDecimal money, Long parentId) {
        subscriptionPlans.addAll(subscriptionPlanRepository.findAll());
        Collections.reverse(subscriptionPlans);
        RegistrationPerson registrationPerson = setPersonDetails(name,email, parentId);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setEmail(registrationPerson.getEmail());
        bankAccount.setBalance(money);
        bankAccountRepository.save(bankAccount);
        registrationPerson.setBankAccount(bankAccount);
        registrationPersonRepository.save(registrationPerson);
        for (SubscriptionPlan subscriptionPlan : subscriptionPlans) {
            if (checkBalance(registrationPerson.getBankAccount().getBalance(), subscriptionPlan.getId()) >= 0) {
                setSubscription(registrationPerson, subscriptionPlan.getId());
                break;
            } else if (subscriptionPlan.getId() == 1) {
                throw new RuntimeException();
            }
        }
        registrationPerson.setIsSubscriptionEnabled(true);
        registrationPersonRepository.save(registrationPerson);
        return registrationPerson;
    }

    //Helper methods
    private int checkBalance(BigDecimal balance, long planId) {
        return balance.compareTo(subscriptionPlanRepository.getSubscriptionPlanById(planId).orElseThrow().getRegistrationFee());
    }

    @Override
    public void setSubscription(RegistrationPerson registrationPerson, long id) {
        BankAccount bank = bankAccountRepository.findByEmail(registrationPerson.getEmail()).orElseThrow();
        registrationPerson.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow());
        BigDecimal balance = bank.getBalance();
        BigDecimal fee = subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow().getRegistrationFee();
        BigDecimal newBalance = balance.subtract(fee);
        bank.setBalance(newBalance);
        bankAccountRepository.save(bank);
    }

    private RegistrationPerson setPersonDetails(String name, String email, Long parentId) {
        RegistrationPerson registrationPerson = new RegistrationPerson();
        registrationPerson.setName(name);

        if (registrationPersonRepository.getFirstByEmail(email).isPresent()){
            throw new RuntimeException("emaila trqq da e unique pich");
        }else {
            registrationPerson.setEmail(email);
        }
        registrationPerson.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
        registrationPerson.setParent(registrationPersonRepository.findById(parentId).orElseThrow());
        //registrationTreeRepository.save(registrationTree);
        return registrationPerson;
    }

    @Override
    public void upgradeSubscriptionPlan(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan){
        if (isUpdateUnavailable(registrationPerson, subscriptionPlan)){
            return;
        }
        registrationPerson.getBankAccount().setBalance(registrationPerson.getBankAccount().getBalance().subtract(subscriptionPlan.getRegistrationFee()));
        registrationPerson.setSubscriptionPlan(subscriptionPlan);
    }

    private static boolean isUpdateUnavailable(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan) {
        return registrationPerson.getSubscriptionPlan().getPercents().length() > subscriptionPlan.getPercents().length()
                || registrationPerson.getBankAccount().getBalance().compareTo(subscriptionPlan.getRegistrationFee().subtract(registrationPerson.getSubscriptionPlan().getRegistrationFee())) < 0;
    }
}
