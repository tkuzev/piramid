package com.example.piramidadjii.configModule;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

public interface ConfigurationService {
    boolean isEligable(SubscriptionPlan subscriptionPlan);

    void transactionBoiler(BankAccount helperBankAccount,
                           RegistrationPerson registrationPerson, SubscriptionPlan registrationPerson1, Description registrationFee);

    //     BigDecimal monthTaxPaid(Person person);
//
//     boolean isTaxPaid(Person person);
}



