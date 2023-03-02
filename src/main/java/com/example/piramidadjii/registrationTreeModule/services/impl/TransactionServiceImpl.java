package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTransaction;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.enums.OperationType;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.TransactionRepository;
import com.example.piramidadjii.registrationTreeModule.services.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class TransactionServiceImpl implements TransactionService {
    private Long percentages = 0L;

    @Autowired
    private TransactionRepository transactionRepository;

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
        OperationType[] operationType = new OperationType[1];
        Long profit = 20L;

        traverseFromNodeToRoot(registrationPerson)
                .limit(5)
                .forEach(node -> setNewTransactions(registrationPerson, price, percent, counter, node, operationType));


        transactionDetails(
                registrationPersonRepository.getRegistrationPersonById(1L).orElseThrow(),
                price,
                profit - percentages,
                OperationType.BONUS, counter);
        percentages=0L;
    }


    private void setNewTransactions(RegistrationPerson person, BigDecimal price, Long[] percent, AtomicInteger counter, RegistrationPerson node, OperationType[] operationType) {
        checkPercent(person, percent, counter, node, operationType);
        percentages += percent[0];
        counter.getAndAdd(1);
        transactionDetails(node, price, percent[0], operationType[0], counter);
    }

    private void checkPercent(RegistrationPerson registrationPerson, Long[] percent, AtomicInteger counter, RegistrationPerson node, OperationType[] operationType) {
        List<Long> percents = mapFromStringToLong(node.getSubscriptionPlan().getPercents());

        if (LocalDate.now().isAfter(registrationPerson.getSubscriptionExpirationDate())) {
            //todo: dali da zapisvame tranzakciq ili da mu eba maikata?1?1?
            return;
        }

        if (node.getId().equals(registrationPerson.getId())) {
            Long FLAT_PERCENTAGE = 5L;
            percent[0] = FLAT_PERCENTAGE;
            operationType[0] = OperationType.SOLD;
        } else if (counter.get() > percents.size()) {
            percent[0] = 0L;
            operationType[0] = OperationType.BONUS;
        } else {
            percent[0] = percents.get(counter.get() - 1);
            operationType[0] = OperationType.BONUS;
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

    private void transactionDetails(RegistrationPerson registrationPerson, BigDecimal price, Long percent, OperationType operationType, AtomicInteger counter) {
        BankAccount bank = registrationPerson.getBankAccount();
        RegistrationTransaction transaction = new RegistrationTransaction();
        transaction.setPercent(percent);
        transaction.setWholePrice(price);
        transaction.setPrice(calculatePrice(percent, price));
        transaction.setRegistrationPerson(registrationPerson);
        transaction.setOperationType(operationType);
        transaction.setDate(LocalDate.now());
        transaction.setLevel(counter.get());
        BigDecimal newBalance = bank.getBalance().add(transaction.getPrice());
        bank.setBalance(bank.getBalance().add(newBalance));
        bankAccountRepository.save(bank);
        registrationPersonRepository.save(registrationPerson);
        transactionRepository.save(transaction);
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

        List<RegistrationTransaction> allTransactions = transactionRepository.
                findAllByRegistrationPersonAndDateBetween(registrationPerson, LocalDate.now().minusMonths(1),LocalDate.now());


        for (RegistrationTransaction transactions : allTransactions) {
            for (SubscriptionPlan s : subscriptionPlans) {
                List<Long> percentages = mapFromStringToLong(s.getPercents());

                if (transactions.getLevel() < percentages.size()) {
                    BigDecimal oldSum = income.get(s);
                    BigDecimal valueToAdd = transactions.getWholePrice().multiply(BigDecimal.valueOf(percentages.get(transactions.getLevel()))).divide(BigDecimal.valueOf(100)).setScale(2);
                    income.put(s, oldSum.add(valueToAdd));
                }
            }

        }

        return income;
    }

}
