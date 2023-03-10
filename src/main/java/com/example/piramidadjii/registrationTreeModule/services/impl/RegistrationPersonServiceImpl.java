package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.entities.Bank;
import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.bankAccountModule.repositories.BankRepository;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.baseModule.enums.OperationType;
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
import java.util.Date;
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

    List<SubscriptionPlan> subscriptionPlans = new ArrayList<>();


    @Override
    @Transactional
    public RegistrationPerson registerPerson(String name, BigDecimal money, Long parentId) {
        subscriptionPlans.addAll(subscriptionPlanRepository.findAll());
        Collections.reverse(subscriptionPlans);
        RegistrationPerson registrationPerson = setPersonDetails(name, parentId);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(money);
        bankAccountRepository.save(bankAccount);
        registrationPerson.setBankAccount(bankAccount);
        registrationPersonRepository.save(registrationPerson);
        subscriptionPlans.stream()
                .filter(x->checkBalance(registrationPerson.getBankAccount().getBalance(), x.getId()) >= 0)
                .findFirst()
                .ifPresent(subscriptionPlan -> {
                        setSubscription(registrationPerson, subscriptionPlan.getId());
                        registrationPerson.setIsSubscriptionEnabled(true);
                        registrationPersonRepository.save(registrationPerson);
                });
//        for (SubscriptionPlan subscriptionPlan : subscriptionPlans) {
//            if (checkBalance(registrationPerson.getBankAccount().getBalance(), subscriptionPlan.getId()) >= 0) {
//                setSubscription(registrationPerson, subscriptionPlan.getId());
//                registrationPerson.setIsSubscriptionEnabled(true);
//                registrationPersonRepository.save(registrationPerson);
//                break;
//            } else if (subscriptionPlan.getId() == 1) {
//
//            }
//        }
        return registrationPerson;
    }

    //Helper methods
    private int checkBalance(BigDecimal balance, long planId) {
        return balance.compareTo(subscriptionPlanRepository.getSubscriptionPlanById(planId).orElseThrow().getRegistrationFee());
    }

    @Override
    public void setSubscription(RegistrationPerson registrationPerson, long id) {
        BankAccount helperBankAccount = bankAccountRepository.findById(-1L).orElseThrow();
        BankAccount bank = bankAccountRepository.findById(registrationPerson.getId()).orElseThrow();
        registrationPerson.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow());
        BigDecimal balance = bank.getBalance();
        BigDecimal fee = subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow().getRegistrationFee();
        BigDecimal newBalance = balance.subtract(fee);
        bank.setBalance(newBalance);
        bankAccountRepository.save(bank);


        Bank debitTransaction = new Bank();
        Bank creditTransaction = new Bank();
        creditTransaction.setDstAccId(helperBankAccount);
        creditTransaction.setSrcAccId(registrationPerson.getBankAccount());
        creditTransaction.setAmount(registrationPerson.getSubscriptionPlan().getRegistrationFee());
        creditTransaction.setOperationType(OperationType.DT);
        creditTransaction.setDescription(Description.REGISTRATION_FEE);
        creditTransaction.setTransactionDate(LocalDateTime.now());
        debitTransaction.setTransactionDate(LocalDateTime.now());
        debitTransaction.setDescription(Description.REGISTRATION_FEE);
        debitTransaction.setOperationType(OperationType.CT);
        debitTransaction.setAmount(registrationPerson.getSubscriptionPlan().getRegistrationFee());
        debitTransaction.setDstAccId(registrationPerson.getBankAccount());
        debitTransaction.setSrcAccId(helperBankAccount);
        bankRepository.save(creditTransaction);
        bankRepository.save(debitTransaction);
    }

    private RegistrationPerson setPersonDetails(String name, Long parentId) {
        RegistrationPerson registrationPerson = new RegistrationPerson();
        registrationPerson.setName(name);
        registrationPerson.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
        registrationPerson.setParent(registrationPersonRepository.findById(parentId).orElseThrow());
        //registrationTreeRepository.save(registrationTree);
        return registrationPerson;
    }

    @Override
    public void upgradeSubscriptionPlan(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan) {
        BankAccount helperBankAccount = bankAccountRepository.findById(-1L).orElseThrow();
        if (isUpdateUnavailable(registrationPerson, subscriptionPlan)) {
            return;
        }
        registrationPerson.getBankAccount().setBalance(registrationPerson.getBankAccount().getBalance().subtract(subscriptionPlan.getRegistrationFee()));
        Bank creditTransaction = new Bank();
        Bank debitTransaction = new Bank();
        debitTransaction.setDstAccId(helperBankAccount);
        debitTransaction.setSrcAccId(registrationPerson.getBankAccount());
        debitTransaction.setAmount(subscriptionPlan.getRegistrationFee());
        debitTransaction.setOperationType(OperationType.DT);
        debitTransaction.setDescription(Description.UPDATE_PLAN_FEE);
        debitTransaction.setTransactionDate(LocalDateTime.now());
        creditTransaction.setTransactionDate(LocalDateTime.now());
        creditTransaction.setDescription(Description.UPDATE_PLAN_FEE);
        creditTransaction.setOperationType(OperationType.CT);
        creditTransaction.setAmount(subscriptionPlan.getRegistrationFee());
        creditTransaction.setDstAccId(registrationPerson.getBankAccount());
        creditTransaction.setSrcAccId(helperBankAccount);
        bankRepository.save(debitTransaction);
        bankRepository.save(creditTransaction);
        registrationPerson.setSubscriptionPlan(subscriptionPlan);
    }

    private static boolean isUpdateUnavailable(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan) {
        return registrationPerson.getSubscriptionPlan().getPercents().length() > subscriptionPlan.getPercents().length()
                || registrationPerson.getBankAccount().getBalance().compareTo(subscriptionPlan.getRegistrationFee().subtract(registrationPerson.getSubscriptionPlan().getRegistrationFee())) < 0;
    }
}
