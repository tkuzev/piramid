package com.example.piramidadjii.services;

import com.example.piramidadjii.entities.Person;

import java.math.BigDecimal;

public interface TransactionService {

    void createTransaction(Person person, BigDecimal price, int counter);

}
