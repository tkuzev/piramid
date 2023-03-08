package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.entities.Bank;
import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.bankAccountModule.repositories.BankRepository;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.baseModule.enums.OperationType;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class TransactionServiceImpl implements TransactionService {
    private Long percentages = 0L;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    @Transactional
    public void createTransaction(RegistrationPerson registrationPerson, BigDecimal price) {
        final Long[] percent = new Long[1];
        AtomicInteger counter = new AtomicInteger(0);
        Description[] description = new Description[1];
        Long profit = 100L;

        traverseFromNodeToRoot(registrationPerson)
                .limit(5)
                .forEach(node -> setNewTransactions(registrationPerson, price, percent, counter, node, description));


        transactionDetails(
                registrationPersonRepository.getRegistrationPersonById(1L).orElseThrow(),
                price,
                profit - percentages,
                Description.MONEY_LEFT, counter);
        percentages = 0L;
    }

    private void setNewTransactions(RegistrationPerson person, BigDecimal price, Long[] percent, AtomicInteger counter, RegistrationPerson node, Description[] description) {
        checkPercent(person, percent, counter, node, description);
        percentages += percent[0];
        counter.getAndAdd(1);
        transactionDetails(node, price, percent[0], description[0], counter);
    }

    private void checkPercent(RegistrationPerson registrationPerson, Long[] percent, AtomicInteger counter, RegistrationPerson node, Description[] description) {
        List<Long> percents = mapFromStringToLong(node.getSubscriptionPlan().getPercents());

        if (LocalDate.now().isAfter(registrationPerson.getSubscriptionExpirationDate())) {
            //todo: dali da zapisvame tranzakciq ili da mu eba maikata?1?1?
            return;
        }

        if (node.getId().equals(registrationPerson.getId())) {
            Long FLAT_PERCENTAGE = 5L;
            percent[0] = FLAT_PERCENTAGE;
            description[0] = Description.SOLD;
        } else if (counter.get() > percents.size()) {
            percent[0] = 0L;
            description[0] = Description.BONUS;
        } else {
            percent[0] = percents.get(counter.get() - 1);
            description[0] = Description.BONUS;
        }
    }

    public Stream<RegistrationPerson> traverseFromNodeToRoot(RegistrationPerson node) {
        if (/* Stop */ Objects.isNull(node) || /* skip company */ Objects.isNull(node.getParent())) {
            return Stream.empty();
        }
        return Stream.concat(Stream.of(node), traverseFromNodeToRoot(node.getParent()));
    }

    private static BigDecimal calculatePrice(Long percent, BigDecimal price) {
        return price.multiply(new BigDecimal(percent)).divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
    }

    private void transactionDetails(RegistrationPerson registrationPerson, BigDecimal price, Long percent, Description description, AtomicInteger counter) {
        BankAccount personBankAccount = registrationPerson.getBankAccount();
        BankAccount helperBankAccount = bankAccountRepository.findById(-1L).orElseThrow();
        Bank debitTransaction = new Bank();
        Bank creditTransaction = new Bank();

        setTr(debitTransaction, percent, price, registrationPerson.getBankAccount(), helperBankAccount, description, OperationType.DT, counter);

        BigDecimal newDebitBalance = personBankAccount.getBalance().add(debitTransaction.getAmount());
        personBankAccount.setBalance(personBankAccount.getBalance().add(newDebitBalance));
        bankAccountRepository.save(personBankAccount);
        registrationPersonRepository.save(registrationPerson);
        bankRepository.save(debitTransaction);

        setTr(creditTransaction, percent, price, helperBankAccount, registrationPerson.getBankAccount(), description, OperationType.CT, counter);

        BigDecimal newCreditBalance = helperBankAccount.getBalance().subtract(debitTransaction.getAmount());
        helperBankAccount.setBalance(helperBankAccount.getBalance().subtract(newCreditBalance));
        bankAccountRepository.save(helperBankAccount);
        bankRepository.save(creditTransaction);
    }

    public List<Long> mapFromStringToLong(String percents) {

        List<Long> list = new ArrayList<>();

        String[] stringArray = percents.split("//");
        for (String string : stringArray) {
            Long longInt = Long.valueOf(string);
            list.add(longInt);
        }
        return list;
    }

    @Override
    public Map<SubscriptionPlan, BigDecimal> monthlyIncome(RegistrationPerson registrationPerson) {
        Map<SubscriptionPlan, BigDecimal> income = new HashMap<>();
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanRepository.findAll();
        for (SubscriptionPlan s : subscriptionPlans) {
            income.put(s, BigDecimal.ZERO);
        }

        List<Bank> allTransactions = bankRepository.
                findAllByIdAndTransactionDateBetween(registrationPerson.getId(), LocalDateTime.now().minusMonths(1), LocalDateTime.now());


        for (Bank transactions : allTransactions) {
            for (SubscriptionPlan s : subscriptionPlans) {
                List<Long> percentages = mapFromStringToLong(s.getPercents());

                if (!transactions.getDescription().equals(Description.REGISTRATION_FEE) && transactions.getLevel() < percentages.size()) {
                    BigDecimal oldSum = income.get(s);
                    BigDecimal valueToAdd = transactions.getItemPrice().multiply(BigDecimal.valueOf(percentages.get(Math.toIntExact(transactions.getLevel())))).divide(BigDecimal.valueOf(100)).setScale(2);
                    income.put(s, oldSum.add(valueToAdd));
                }
            }
        }

        return income;
    }
    private static void setTr(Bank transaction, Long percent, BigDecimal price, BankAccount registrationPerson, BankAccount helperBankAccount, Description description, OperationType dt, AtomicInteger counter) {
        transaction.setPercent(percent);
        transaction.setItemPrice(price);
        transaction.setAmount(calculatePrice(percent, price));
        transaction.setDstAccId(registrationPerson);
        transaction.setSrcAccId(helperBankAccount);
        transaction.setDescription(description);
        transaction.setOperationType(dt);
        transaction.setTransactionDate(LocalDateTime.now());

        if (registrationPerson.getId()!=1L && registrationPerson.getId()!=-1L) {
            transaction.setLevel((long) counter.get()-1);
        }
    }
}
