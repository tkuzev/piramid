package com.example.piramidadjii.bankAccountModule.services;

import java.math.BigDecimal;

public interface BankService {

    void deposit(Long id, BigDecimal money);

    void withdraw(Long id, BigDecimal money);
}
