package com.example.piramidadjii.binaryTreeModule.services;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;

import java.math.BigDecimal;

public interface DistributeMoneyService {
    void distributeMoney(BinaryPerson person, BigDecimal money);
}
