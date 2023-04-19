package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface TransactionService {
    void createTransaction(Long registrationPersonId, BigDecimal price);

    Map<String, BigDecimal> monthlyIncome(Long id);

    List<BigDecimal> wallet(Long registrationPersonId);
}
