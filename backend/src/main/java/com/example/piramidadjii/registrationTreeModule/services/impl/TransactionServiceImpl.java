package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.entities.Bank;
import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.bankAccountModule.repositories.BankRepository;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.baseModule.enums.OperationType;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.configModule.ConfigurationService;
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
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private BinaryPersonRepository binaryPersonRepository;

    private static final long HELPER_BANK_ID = -1;

    @Override
    @Transactional
    public void createTransaction(RegistrationPerson registrationPerson, BigDecimal price) {
        final Long[] percent = new Long[1];
        AtomicInteger counter = new AtomicInteger(0);
        Description[] description = new Description[1];
        Long profit = 100L;

        traverseFromNodeToRoot(registrationPerson).limit(5).forEach(node -> setNewTransactions(registrationPerson, price, percent, counter, node, description));

        transactionDetails(registrationPersonRepository.getRegistrationPersonById(1L).orElseThrow(), price, profit - percentages, Description.MONEY_LEFT, counter);

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
            long FLAT_PERCENTAGE = 5L;
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
        BankAccount helperBankAccount = bankAccountRepository.findById(HELPER_BANK_ID).orElseThrow();


        if (registrationPerson.getId() != 1L && registrationPerson.getId() != HELPER_BANK_ID) {
            configurationService.transactionBoiler(helperBankAccount, registrationPerson, description, price, counter.get() - 1, percent);
        }

        BigDecimal amount = price.multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100).setScale(2, RoundingMode.FLOOR));
        BigDecimal newDebitBalance = personBankAccount.getBalance().add(amount);
        personBankAccount.setBalance(personBankAccount.getBalance().add(newDebitBalance));
        bankAccountRepository.save(personBankAccount);
        registrationPersonRepository.save(registrationPerson);

        BigDecimal newCreditBalance = helperBankAccount.getBalance().subtract(amount);
        helperBankAccount.setBalance(helperBankAccount.getBalance().subtract(newCreditBalance));
        bankAccountRepository.save(helperBankAccount);
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

    //todo
    @Override
    public Map<String, BigDecimal> monthlyIncome(Long id) {
        Map<String, BigDecimal> income = new HashMap<>();
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanRepository.findAll();

        for (SubscriptionPlan s : subscriptionPlans) {
            income.put(s.getName(), BigDecimal.ZERO);
        }
        income.put("SOLD",BigDecimal.ZERO);
        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow();
        List<Bank> allBonusTransactions = bankRepository.findAllByDescriptionAndDstAccIdAndTransactionDateBetweenAndOperationType(Description.BONUS, bankAccount, LocalDateTime.now().minusMonths(1), LocalDateTime.now(),OperationType.DT);

        for (Bank transactions : allBonusTransactions) {
            for (SubscriptionPlan s : subscriptionPlans) {
                List<Long> percentages = mapFromStringToLong(s.getPercents());

                if (transactions.getLevel() - 1 < percentages.size()) {
                    BigDecimal oldSum = income.get(s.getName());
                    BigDecimal valueToAdd = transactions.getItemPrice().multiply(BigDecimal.valueOf(percentages.get(Math.toIntExact(transactions.getLevel() - 1)))).divide(BigDecimal.valueOf(100)).setScale(2);
                    income.put(s.getName(), oldSum.add(valueToAdd));
                }
            }
        }
        List<Bank> allSoldTransactions = bankRepository.findAllByDescriptionAndDstAccIdAndTransactionDateBetweenAndOperationType(Description.SOLD,bankAccount,LocalDateTime.now().minusMonths(1), LocalDateTime.now(), OperationType.DT);
        for (Bank transaction: allSoldTransactions){
            BigDecimal oldSum = income.get("SOLD");
            income.put("SOLD",oldSum.add(transaction.getAmount()));
        }
        return income;
    }

    @Override
    public List<BigDecimal> wallet(Long registrationPersonId) {
        if (binaryPersonRepository.existsById(registrationPersonId)) {
            BinaryPerson binPerson = binaryPersonRepository.findById(registrationPersonId).orElseThrow();
            return Arrays.asList(binPerson.getBankAccount().getBalance(), binPerson.getLeftContainer(),
                    binPerson.getRightContainer());
        }
        RegistrationPerson regPerson = registrationPersonRepository.findById(registrationPersonId).orElseThrow();
        return Collections.singletonList(regPerson.getBankAccount().getBalance());
    }
}
