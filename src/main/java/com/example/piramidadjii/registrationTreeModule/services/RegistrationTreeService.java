package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.entities.Person;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;

import java.math.BigDecimal;

public interface RegistrationTreeService {

    void registerPerson(RegistrationTree registrationTree);

    void sell(BigDecimal sellPrice, RegistrationTree registrationTree);

    void initialFee(RegistrationTree registrationTree);
}
