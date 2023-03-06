package com.example.piramidadjii.configModule.impl;

import com.example.piramidadjii.baseEntities.Person;
import com.example.piramidadjii.configModule.ConfigurationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    RegistrationPersonRepository registrationPersonRepository;
    @Override
    public boolean isEligable(SubscriptionPlan subscriptionPlan) {
        return subscriptionPlan.isEligibleForBinary();
    }

    @Override
    public BigDecimal monthTaxPaid(Person person) {
        RegistrationPerson registrationPerson = registrationPersonRepository.getRegistrationPersonById(person.getId()).orElseThrow();
        return registrationPerson.getSubscriptionPlan().getRegistrationFee();
    }

    @Override
    public boolean isTaxPaid(Person person) {
        RegistrationPerson registrationPerson = registrationPersonRepository.getRegistrationPersonById(person.getId()).orElseThrow();
        return registrationPerson.getSubscriptionExpirationDate().isBefore(LocalDate.now());
    }
}
