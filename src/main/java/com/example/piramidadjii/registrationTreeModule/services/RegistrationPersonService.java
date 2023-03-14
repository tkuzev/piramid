package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

import java.math.BigDecimal;


public interface RegistrationPersonService {

    RegistrationPerson registerPerson(String name, String email,BigDecimal money, Long parentId);

    void setSubscription(RegistrationPerson registrationPerson, long id);
}
