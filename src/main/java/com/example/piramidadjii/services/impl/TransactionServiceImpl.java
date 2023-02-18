package com.example.piramidadjii.services.impl;

import com.example.piramidadjii.entities.Person;
import com.example.piramidadjii.entities.Transaction;
import com.example.piramidadjii.repositories.TransactionRepository;
import com.example.piramidadjii.services.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class TransactionServiceImpl implements TransactionService {
    private Long FLAT_PERCENTAGE = 5L;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private SubscriptionPlanServiceImpl subscriptionPlanService;

    @Override
    @Transactional
    public void createTransaction(Person person, BigDecimal price) {
        final Long[] percent = new Long[1];
        AtomicInteger counter = new AtomicInteger(0);

        traverseFromNodeToRoot(person)
                // removed .parallel() shtoto se nasira
                .limit(5) // one more transaction for the root node must be added nqkoga
                .forEach(node -> setNewTransactions(person, price, percent, counter, node));
    }

    private void setNewTransactions(Person person, BigDecimal price, Long[] percent, AtomicInteger counter, Person node) {
        checkPercent(person, percent, counter, node);
        counter.getAndAdd(1);
        returnNewValue(node, price, percent[0]);
    }

    private void checkPercent(Person person, Long[] percent, AtomicInteger counter, Person node) {
        List<Long> percents = subscriptionPlanService.mapFromStringToLong(person.getSubscriptionPlan().getPercents());

        if (node.getId().equals(person.getId())) {
            percent[0] = FLAT_PERCENTAGE;
        } else if (counter.get() > percents.size()) {
            percent[0] = 0L;
        } else {
            percent[0] = percents.get(counter.get() - 1);
        }
    }

    public Stream<Person> traverseFromNodeToRoot(Person node) {
        if (/* Stop */ Objects.isNull(node) || /* skip company */ Objects.isNull(node.getParent())) {
            return Stream.empty();
        }

        return Stream.concat(Stream.of(node), traverseFromNodeToRoot(node.getParent()));
    }

    private static BigDecimal calculatePrice(Long percent, BigDecimal price) {
        return price.multiply(new BigDecimal(percent)).divide(new BigDecimal(100), RoundingMode.HALF_UP);
    }

    public BigDecimal returnNewValue(Person person, BigDecimal sellingPrice, Long percent) {
        person.setBalance(person.getBalance().add(sellingPrice.multiply(BigDecimal.valueOf(percent))));
        transactionDetails(person, sellingPrice, percent);
        return person.getBalance();
    }

    private void transactionDetails(Person person, BigDecimal price, Long percent) {
        Transaction transaction = new Transaction();
        transaction.setPercent(percent);
        transaction.setPrice(calculatePrice(percent, price));
        transaction.setPersonId(person.getId());
        transactionRepository.save(transaction);
    }
}
