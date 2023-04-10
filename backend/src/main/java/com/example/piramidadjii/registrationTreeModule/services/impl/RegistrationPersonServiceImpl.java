package com.example.piramidadjii.registrationTreeModule.services.impl;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.bankAccountModule.services.BankService;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.configModule.ConfigurationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.RoleRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class RegistrationPersonServiceImpl implements RegistrationPersonService {

    private static final long BOSS_BANK_ACCOUNT_ID = 1;

    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BankService bankService;

    @Override
    public RegistrationPerson registerPerson(RegistrationPerson registrationPerson, BigDecimal accountDeposit) {

        // find subscription plans
        List<SubscriptionPlan> subscriptionPlans =
                subscriptionPlanRepository.findAll()
                        .stream()
                        .sorted(Comparator.comparing(SubscriptionPlan::getRegistrationFee).reversed())
                        .toList();

        // create new bank account and deposit
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(accountDeposit);
        bankAccountRepository.save(bankAccount);

        //register person
        registrationPerson.setBankAccount(bankAccount);
        registrationPerson.setPassword(passwordEncoder.encode(registrationPerson.getPassword()));
        registrationPerson.setSubscriptionEnabled(true);
        registrationPerson.setRole(roleRepository.getRoleByName("klient").orElseThrow());
        registrationPersonRepository.save(registrationPerson);

        // find apropriate plan
        subscriptionPlans.stream()
                .filter(x -> checkBalance(registrationPerson.getBankAccount().getBalance(), x.getId()) >= 0)
                .findFirst()
                .ifPresent(subscriptionPlan -> {
                    setSubscription(registrationPerson, subscriptionPlan.getId());
                    registrationPerson.setSubscriptionEnabled(true);
                    registrationPerson.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
                    registrationPersonRepository.save(registrationPerson);
                });


        return registrationPerson;
    }

    //Helper methods
    private int checkBalance(BigDecimal balance, long planId) {
        return balance.compareTo(subscriptionPlanRepository.getSubscriptionPlanById(planId).orElseThrow().getRegistrationFee());
    }

    //todo
    @Override
    public void setSubscription(RegistrationPerson registrationPerson, long id) {
        BankAccount bossBankAccount = bankAccountRepository.findById(BOSS_BANK_ACCOUNT_ID).orElseThrow();
        BankAccount personBankAccount = bankAccountRepository.findById(registrationPerson.getId()).orElseThrow();

        BigDecimal personBalance = personBankAccount.getBalance();
        BigDecimal bossBalance = bossBankAccount.getBalance();

        BigDecimal fee = subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow().getRegistrationFee();
        BigDecimal newBalance = personBalance.subtract(fee);

        registrationPerson.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(id).orElseThrow());

        personBankAccount.setBalance(newBalance);
        bossBankAccount.setBalance(bossBalance.add(fee));

        bankAccountRepository.save(personBankAccount);
        bankAccountRepository.save(bossBankAccount);

        configurationService.transactionBoiler(bossBankAccount, registrationPerson, registrationPerson.
                getSubscriptionPlan(), Description.REGISTRATION_FEE, registrationPerson.getSubscriptionPlan().getRegistrationFee());
    }

    @Override
    public void editPerson(RegistrationPerson registrationPerson) {
        RegistrationPerson personToEdit = registrationPersonRepository.findById(registrationPerson.getId()).orElseThrow();
        personToEdit.setEmail(registrationPerson.getEmail());
        personToEdit.setName(registrationPerson.getName());
        personToEdit.setSubscriptionEnabled(registrationPerson.getSubscriptionEnabled());
        personToEdit.setPassword(passwordEncoder.encode(registrationPerson.getPassword()));
        registrationPersonRepository.save(personToEdit);
    }

    @Override
    public RegistrationPerson displayPersonDetails(String email) {
        return registrationPersonRepository.getByEmail(email).orElseThrow();
    }


}
