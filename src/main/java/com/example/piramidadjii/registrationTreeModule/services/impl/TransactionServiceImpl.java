package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.entities.Transaction;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.enums.OperationType;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationTreeRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.TransactionRepository;
import com.example.piramidadjii.registrationTreeModule.services.SubscriptionPlanService;
import com.example.piramidadjii.registrationTreeModule.services.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class TransactionServiceImpl implements TransactionService {
    private Long FLAT_PERCENTAGE = 5L;
    private Long percentages = 0L;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private SubscriptionPlanServiceImpl subscriptionPlanService;
    @Autowired
    private RegistrationTreeRepository registrationTreeRepository;

    @Override
    @Transactional
    public void createTransaction(RegistrationTree registrationTree, BigDecimal price) {
        final Long[] percent = new Long[1];
        AtomicInteger counter = new AtomicInteger(0);
        OperationType[] operationType = new OperationType[1];
        Long profit = 20L; //How exactly to calculate the bonus of the root?

        traverseFromNodeToRoot(registrationTree)
                // removed .parallel() shtoto se nasira
                .limit(5) // one more transaction for the root node must be added nqkoga
                .forEach(node -> setNewTransactions(registrationTree, price, percent, counter, node, operationType));

        transactionDetails(
                registrationTreeRepository.getRegistrationTreeById(1L).orElseThrow(),
                price,
                profit - percentages,
                OperationType.BONUS);
    }

    private void setNewTransactions(RegistrationTree person, BigDecimal price, Long[] percent, AtomicInteger counter, RegistrationTree node, OperationType[] operationType) {
        checkPercent(person, percent, counter, node, operationType);
        percentages+=percent[0];
        counter.getAndAdd(1);
        transactionDetails(node, price, percent[0], operationType[0]);
    }

    private void checkPercent(RegistrationTree registrationTree, Long[] percent, AtomicInteger counter, RegistrationTree node, OperationType[] operationType) {
        List<Long> percents = subscriptionPlanService.mapFromStringToLong(node.getSubscriptionPlan().getPercents());

        if (!LocalDate.now().isAfter(registrationTree.getSubscriptionExpirationDate())){
            //todo: dali da zapisvame tranzakciq ili da mu eba maikata?1?1?
            return;
        }

        if (node.getId().equals(registrationTree.getId())) {
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

    public Stream<RegistrationTree> traverseFromNodeToRoot(RegistrationTree node) {
        if (/* Stop */ Objects.isNull(node) || /* skip company */ Objects.isNull(node.getRegistrationTree())) {
            return Stream.empty();
        }

        return Stream.concat(Stream.of(node), traverseFromNodeToRoot(node.getRegistrationTree()));
    }

    private static BigDecimal calculatePrice(Long percent, BigDecimal price) {
        return price.multiply(new BigDecimal(percent)).divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
    }

    private void transactionDetails(RegistrationTree registrationTree, BigDecimal price, Long percent, OperationType operationType) {
        Transaction transaction = new Transaction();
        transaction.setPercent(percent);
        transaction.setPrice(calculatePrice(percent, price));
        transaction.setRegistrationTree(registrationTree);
        transaction.setOperationType(operationType);
        BigDecimal newBalance = registrationTree.getBalance().add(transaction.getPrice());
        registrationTree.setBalance(newBalance);
        registrationTreeRepository.save(registrationTree);
        transactionRepository.save(transaction);
    }
}
