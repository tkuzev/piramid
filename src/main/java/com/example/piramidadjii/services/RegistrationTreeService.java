package com.example.piramidadjii.services;

import com.example.piramidadjii.entities.Person;
import com.example.piramidadjii.entities.Plan;

import java.math.BigDecimal;

public interface RegistrationTreeService {

    void registerPerson(Person person, Plan plan);

    void sell(BigDecimal sellPrice, Person person);

    void initialFee(Person person,BigDecimal tax);
}
