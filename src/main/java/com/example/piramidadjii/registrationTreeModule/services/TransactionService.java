package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;

import java.math.BigDecimal;


public interface TransactionService {
    void createTransaction(RegistrationPerson registrationPerson, BigDecimal price);
}
