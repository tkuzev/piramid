package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

import java.math.BigDecimal;


public interface RegistrationPersonService {

    RegistrationPerson registerPerson(RegistrationPerson registrationPerson, BigDecimal money);

    void setSubscription(RegistrationPerson registrationPerson, long id);
}
