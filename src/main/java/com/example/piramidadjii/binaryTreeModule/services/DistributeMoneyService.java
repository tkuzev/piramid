package com.example.piramidadjii.binaryTreeModule.services;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryTree;

import java.math.BigDecimal;
public interface DistributeMoneyService {
    void distributeMoney(BinaryTree person, BigDecimal money);
}
