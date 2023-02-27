package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.bankAccountModule.services.BankAccountService;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationTreeRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RegistrationTreeServiceImpl implements RegistrationTreeService {
    @Autowired
    private RegistrationTreeRepository registrationTreeRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private BinaryRegistrationService binaryRegistrationService;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    List<SubscriptionPlan> subscriptionPlans = new ArrayList<>();


    @Override
    public RegistrationTree registerPerson(String name,  String email, BigDecimal money, Long parentId) {
        subscriptionPlans.addAll(subscriptionPlanRepository.findAll());
        Collections.reverse(subscriptionPlans);
        RegistrationTree registrationTree = setPersonDetails(name,email, parentId);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setEmail(registrationTree.getEmail());
        bankAccount.setBalance(money);
        bankAccountRepository.save(bankAccount);
        registrationTree.setBankAccount(bankAccount);
        registrationTreeRepository.save(registrationTree);
        for (SubscriptionPlan subscriptionPlan : subscriptionPlans) {
            if (checkBalance(registrationTree.getBankAccount().getBalance(), subscriptionPlan.getId()) >= 0) {
                setSubscription(registrationTree, subscriptionPlan.getId());
                break;
            } else if (subscriptionPlan.getId() == 1) {
                throw new RuntimeException();
            }
        }
        registrationTreeRepository.save(registrationTree);
        return registrationTree;
    }

    //Helper methods
    private int checkBalance(BigDecimal balance, long planId) {
        return balance.compareTo(subscriptionPlanRepository.getSubscriptionPlanById(planId).orElseThrow().getRegistrationFee());
    }

    @Override
    public void setSubscription(RegistrationTree registrationTree, long id) {
        BankAccount bank = bankAccountRepository.findByEmail(registrationTree.getEmail()).orElseThrow();
        registrationTree.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow());
        BigDecimal balance = bank.getBalance();
        BigDecimal fee = subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow().getRegistrationFee();
        BigDecimal newBalance = balance.subtract(fee);
        bank.setBalance(newBalance);
        bankAccountRepository.save(bank);
    }

    private RegistrationTree setPersonDetails(String name, String email, Long parentId) {
        RegistrationTree registrationTree = new RegistrationTree();
        registrationTree.setName(name);

        if (registrationTreeRepository.getFirstByEmail(email).isPresent()){
            throw new RuntimeException("emaila trqq da e unique pich");
        }else {
            registrationTree.setEmail(email);
        }
        registrationTree.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
        registrationTree.setParent(registrationTreeRepository.findById(parentId).orElseThrow());
        //registrationTreeRepository.save(registrationTree);
        return registrationTree;
    }

    @Override
    public void upgradeSubscriptionPlan(RegistrationTree registrationTree,SubscriptionPlan subscriptionPlan){
        if (isUpdateUnavailable(registrationTree, subscriptionPlan)){
            return;
        }
        registrationTree.getBankAccount().setBalance(registrationTree.getBankAccount().getBalance().subtract(subscriptionPlan.getRegistrationFee()));
        registrationTree.setSubscriptionPlan(subscriptionPlan);
    }

    private static boolean isUpdateUnavailable(RegistrationTree registrationTree, SubscriptionPlan subscriptionPlan) {
        return registrationTree.getSubscriptionPlan().getPercents().length() > subscriptionPlan.getPercents().length()
                || registrationTree.getBankAccount().getBalance().compareTo(subscriptionPlan.getRegistrationFee().subtract(registrationTree.getSubscriptionPlan().getRegistrationFee())) < 0;
    }
}
