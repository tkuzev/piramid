package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface TransactionService {

    void createTransaction(RegistrationTree registrationTree, BigDecimal price);


}
