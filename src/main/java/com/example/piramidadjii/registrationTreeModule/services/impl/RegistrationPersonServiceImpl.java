package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.baseModule.enums.Description;
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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class RegistrationPersonServiceImpl implements RegistrationPersonService {
    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private ConfigurationService configurationService;
    private static final long BOSS_BANK_ACCOUNT_ID = 1;

    @Override
    @Transactional
    public RegistrationPerson registerPerson(String name,String email ,BigDecimal money, Long parentId) {
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanRepository.findAll().stream().sorted
                (Comparator.comparing(SubscriptionPlan::getRegistrationFee).reversed()).toList();
        RegistrationPerson registrationPerson = setPersonDetails(name,email ,parentId);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(money);
        bankAccountRepository.save(bankAccount);
        registrationPerson.setBankAccount(bankAccount);
        registrationPersonRepository.save(registrationPerson);
        subscriptionPlans.stream()
                .filter(x -> checkBalance(registrationPerson.getBankAccount().getBalance(), x.getId()) >= 0)
                .findFirst()
                .ifPresent(subscriptionPlan -> {
                    setSubscription(registrationPerson, subscriptionPlan.getId());
                    registrationPerson.setIsSubscriptionEnabled(true);
                    registrationPersonRepository.save(registrationPerson);
                });

        if(Objects.isNull(registrationPerson.getSubscriptionPlan()))
            throw new RuntimeException();

        return registrationPerson;
    }

    //Helper methods
    private int checkBalance(BigDecimal balance, long planId) {
        return balance.compareTo(subscriptionPlanRepository.getSubscriptionPlanById(planId).orElseThrow().getRegistrationFee());
    }

    @Override
    public void setSubscription(RegistrationPerson registrationPerson, long id) {
        BankAccount bossBankAccount = bankAccountRepository.findById(BOSS_BANK_ACCOUNT_ID).orElseThrow();
        BankAccount personBankAccount = bankAccountRepository.findById(registrationPerson.getId()).orElseThrow();

        BigDecimal personBalance = personBankAccount.getBalance();
        BigDecimal bossBalance = bossBankAccount.getBalance();

        BigDecimal fee = subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow().getRegistrationFee();
        BigDecimal newBalance = personBalance.subtract(fee);

        registrationPerson.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow());
        personBankAccount.setBalance(newBalance);
        bossBankAccount.setBalance(bossBalance.add(fee));

        bankAccountRepository.save(personBankAccount);
        bankAccountRepository.save(bossBankAccount);

        configurationService.transactionBoiler(bossBankAccount, registrationPerson, registrationPerson.
                getSubscriptionPlan(), Description.REGISTRATION_FEE,registrationPerson.getSubscriptionPlan().getRegistrationFee());
    }

    private RegistrationPerson setPersonDetails(String name,String email ,Long parentId) {
        return RegistrationPerson.builder()
                .name(name)
                .email(email)
                .subscriptionExpirationDate(LocalDate.now().plusMonths(1))
                .parent(registrationPersonRepository.findById(parentId).orElseThrow())
                .build();
    }
}
