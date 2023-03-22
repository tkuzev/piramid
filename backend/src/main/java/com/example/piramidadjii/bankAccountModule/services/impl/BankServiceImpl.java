package com.example.piramidadjii.bankAccountModule.services.impl;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.bankAccountModule.services.BankService;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.configModule.ConfigurationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankServiceImpl implements BankService {
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    ConfigurationService configurationService;

    @Autowired
    RegistrationPersonRepository registrationPersonRepository;

    BankAccount helperBankAccount;

    @PostConstruct
    private void construct() {
        this.helperBankAccount = bankAccountRepository.findById(-1L).orElseThrow();
    }

    @Override
    public void deposit(Long id, BigDecimal money) {
        RegistrationPerson person = registrationPersonRepository.findById(id).orElseThrow();
        person.getBankAccount().setBalance(person.getBankAccount().getBalance().add(money));
        configurationService.transactionBoiler(helperBankAccount, person, person.getSubscriptionPlan(), Description.DEPOSIT, money);
        bankAccountRepository.save(person.getBankAccount());
    }

    @Override
    public void withdraw(Long id, BigDecimal money) {
        RegistrationPerson person = registrationPersonRepository.findById(id).orElseThrow();
        person.getBankAccount().setBalance(person.getBankAccount().getBalance().subtract(money));
        configurationService.transactionBoiler(helperBankAccount, person, person.getSubscriptionPlan(), Description.WITHDRAW, money);
        bankAccountRepository.save(person.getBankAccount());
    }
}
