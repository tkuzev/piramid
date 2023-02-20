package com.example.piramidadjii.services;

import com.example.piramidadjii.entities.Person;
import com.example.piramidadjii.enums.OperationType;

import java.math.BigDecimal;

public interface TransactionService {

    void createTransaction(Person person, BigDecimal price);



}
