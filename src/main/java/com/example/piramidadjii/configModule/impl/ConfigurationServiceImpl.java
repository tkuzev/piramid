package com.example.piramidadjii.configModule.impl;

import com.example.piramidadjii.bankAccountModule.entities.Bank;
import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankRepository;
import com.example.piramidadjii.baseModule.baseEntites.Person;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.baseModule.enums.OperationType;
import com.example.piramidadjii.configModule.ConfigurationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    RegistrationPersonRepository registrationPersonRepository;

    @Autowired
    BankRepository bankRepository;

    @Override
    public boolean isEligable(SubscriptionPlan subscriptionPlan) {
        return subscriptionPlan.isEligibleForBinary();
    }

    //
//    @Override
//    public BigDecimal monthTaxPaid(Person person) {
//        RegistrationPerson registrationPerson = registrationPersonRepository.getRegistrationPersonById(person.getId()).orElseThrow();
//        return registrationPerson.getSubscriptionPlan().getRegistrationFee();
//    }
//
//    @Override
//    public boolean isTaxPaid(Person person) {
//        RegistrationPerson registrationPerson = registrationPersonRepository.getRegistrationPersonById(person.getId()).orElseThrow();
//        return registrationPerson.getSubscriptionExpirationDate().isBefore(LocalDate.now());
//    }
    @Override
    public void transactionBoiler(BankAccount helperBankAccount, RegistrationPerson registrationPerson, SubscriptionPlan registrationPerson1, Description registrationFee) {
        Bank debitTransaction = Bank.builder()
                .amount(registrationPerson1.getRegistrationFee().negate())
                .srcAccId(helperBankAccount)
                .dstAccId(registrationPerson.getBankAccount())
                .description(registrationFee)
                .operationType(OperationType.CT)
                .transactionDate(LocalDateTime.now())
                .build();
        Bank creditTransaction = Bank.builder()
                .amount(registrationPerson1.getRegistrationFee())
                .srcAccId(registrationPerson.getBankAccount())
                .dstAccId(helperBankAccount)
                .description(registrationFee)
                .operationType(OperationType.DT)
                .transactionDate(LocalDateTime.now())
                .build();

//        creditTransaction.setDstAccId(helperBankAccount);
//        creditTransaction.setSrcAccId(registrationPerson.getBankAccount());
//        creditTransaction.setAmount(registrationPerson1.getRegistrationFee());
//        creditTransaction.setOperationType(OperationType.DT);
//        creditTransaction.setDescription(registrationFee);
//        creditTransaction.setTransactionDate(LocalDateTime.now());
//        debitTransaction.setTransactionDate(LocalDateTime.now());
//        debitTransaction.setDescription(registrationFee);
//        debitTransaction.setOperationType(OperationType.CT);
//        debitTransaction.setAmount(registrationPerson1.getRegistrationFee().negate());
//        debitTransaction.setDstAccId(registrationPerson.getBankAccount());
//        debitTransaction.setSrcAccId(helperBankAccount);
        bankRepository.save(creditTransaction);
        bankRepository.save(debitTransaction);

    }
}
