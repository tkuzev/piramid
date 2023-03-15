package com.example.piramidadjii.bankAccountModule.services.impl;

import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.bankAccountModule.services.BankService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankServiceImpl implements BankService {
    @Autowired
    BankAccountRepository bankAccountRepository;

    @Override
    public void deposit(RegistrationPerson person, BigDecimal money){
        person.getBankAccount().setBalance(person.getBankAccount().getBalance().add(money));
        bankAccountRepository.save(person.getBankAccount());
    }

    @Override
    public void withdraw(RegistrationPerson person, BigDecimal money){
        person.getBankAccount().setBalance(person.getBankAccount().getBalance().subtract(money));
        bankAccountRepository.save(person.getBankAccount());
    }
}
