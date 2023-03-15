package com.example.piramidadjii.bankAccountModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;

import java.math.BigDecimal;

public interface BankService {

    void deposit(RegistrationPerson person, BigDecimal money);

    void withdraw(RegistrationPerson person, BigDecimal money);
}
