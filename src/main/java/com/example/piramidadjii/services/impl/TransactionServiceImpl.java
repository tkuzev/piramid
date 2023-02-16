package com.example.piramidadjii.services.impl;

import com.example.piramidadjii.entities.Person;
import com.example.piramidadjii.entities.Transaction;
import com.example.piramidadjii.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.repositories.TransactionRepository;
import com.example.piramidadjii.services.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Service
public class TransactionServiceImpl implements TransactionService {

    private BigDecimal FLAT_PERCENTAGE = BigDecimal.valueOf(5L);

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    @Transactional
    public void createTransaction(Person person, BigDecimal price) {
        final BigDecimal[] percent = new BigDecimal[1];
        AtomicInteger counter = new AtomicInteger();
        traverseFromNodeToRoot(person)
                .parallel()
                .limit(5)
                .forEach(node -> setNewTransactions(person, price, percent, counter, node));
    }

    private void setNewTransactions(Person person, BigDecimal price, BigDecimal[] percent, AtomicInteger counter, Person node) {
        checkPercent(person, percent, counter, node);
        counter.addAndGet(1);
        returnNewValue(node, price, percent[0]);
    }
    private void checkPercent(Person person, BigDecimal[] percent, AtomicInteger counter, Person node) {
        if (node.getId().equals(person.getId())) {
            percent[0] = FLAT_PERCENTAGE;
        } else if (counter.get() > node.getSubscriptionPlan().getLevels()) {
            percent[0] = BigDecimal.ZERO;
        } else {
            percent[0] = node.getSubscriptionPlan().getPercent();
        }
    }
    public Stream<Person> traverseFromNodeToRoot(Person node) {
        //
        if (/* Stop */ Objects.isNull(node) || /* skip company */ Objects.isNull(node.getParent())) {
            return Stream.empty();
        }
        return Stream.concat(Stream.of(node), traverseFromNodeToRoot(node.getParent()));
    }

    private static BigDecimal calculatePrice(BigDecimal percent, BigDecimal price) {
        return percent.multiply(price).divide(new BigDecimal(100));
    }

    public BigDecimal returnNewValue(Person person, BigDecimal sellingPrice, BigDecimal percent) {
        person.setBalance(person.getBalance().add(sellingPrice.multiply(percent)));
        transactionDetails(person, sellingPrice, percent);
        return person.getBalance();
    }

    private void transactionDetails(Person person, BigDecimal price, BigDecimal percent) {
        Transaction transaction = new Transaction();
        transaction.setPercent(percent);
        transaction.setPrice(calculatePrice(percent, price));
        transaction.setPersonId(person.getId());
        transactionRepository.save(transaction);
    }

    private static BigDecimal calculatePrice(BigDecimal percent, BigDecimal price) {
        return percent.multiply(price).divide(new BigDecimal(100));
    }

}
