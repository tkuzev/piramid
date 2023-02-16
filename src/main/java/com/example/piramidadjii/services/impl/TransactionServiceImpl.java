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

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SubscriptionPlanRepository  subscriptionPlanRepository;



    private final BigDecimal FLAT_PERCENTAGE = BigDecimal.valueOf(5L);




    @Override
    @Transactional
    public void createTransaction(Person person, BigDecimal price, int counter) {

        if (counter < subscriptionPlanRepository.count() + 1 && person.getParent() != null ) {
            if (counter == 0) {
                transactionDetails(person, FLAT_PERCENTAGE, price);
            } else {
                if (person.getSubscriptionPlan().getLevels() < counter) {
                    transactionDetails(person, BigDecimal.ZERO, price);
                } else {
                    transactionDetails(person, person.getSubscriptionPlan().getPercent(), price);
                }
            }
            createTransaction(person.getParent(), price, ++counter);
        }
    }

    //Helper methods
    private void transactionDetails(Person person, BigDecimal percent, BigDecimal price) {
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
