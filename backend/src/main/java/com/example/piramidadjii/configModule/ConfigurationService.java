package com.example.piramidadjii.configModule;

import com.example.piramidadjii.bankAccountModule.entities.Bank;
import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

import java.math.BigDecimal;

public interface ConfigurationService {
    Bank transactionBoiler(BankAccount helperBankAccount,
                           RegistrationPerson registrationPerson, SubscriptionPlan registrationPerson1, Description registrationFee, BigDecimal amount);

    Bank transactionBoiler(BankAccount helperBankAccount, RegistrationPerson registrationPerson,
                           Description description, BigDecimal price,
                           long level, long percent);
}



