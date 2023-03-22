package com.example.piramidadjii.facade.impl;

import com.example.piramidadjii.bankAccountModule.services.BankService;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.facade.FacadeService;
import com.example.piramidadjii.orchestraModule.OrchestraService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.services.SubscriptionPlanService;
import com.example.piramidadjii.registrationTreeModule.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class FacadeServiceImpl implements FacadeService {
    @Autowired
    private OrchestraService orchestraService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private BankService bankService;
    @Autowired
    SubscriptionPlanService subscriptionPlanService;

    @Autowired
    BinaryRegistrationService binaryRegistrationService;


    @Override
    public void registerPerson(RegistrationPerson registrationPerson,Long parentId, BigDecimal money) {
        orchestraService.registerPerson(registrationPerson,parentId, money);
    }

    @Override
    public Map<SubscriptionPlan, BigDecimal> monthlyIncome(Long id) {
        return transactionService.monthlyIncome(id);
    }

    @Override
    public void deposit(Long id, BigDecimal money) {
        bankService.deposit(id, money);
    }

    @Override
    public void withdraw(Long id, BigDecimal money) {
        bankService.withdraw(id, money);
    }

    @Override
    public void upgradeSubscriptionPlan(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan) {
        subscriptionPlanService.upgradeSubscriptionPlan(registrationPerson, subscriptionPlan);
    }

    @Override
    public void createTransaction(RegistrationPerson registrationPerson, BigDecimal price) {
        transactionService.createTransaction(registrationPerson,price);
    }

    @Override
    public List<BigDecimal> wallet(Long registrationPersonId) {
        return transactionService.wallet(registrationPersonId);
    }
}
