package com.example.piramidadjii.facade;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

import java.math.BigDecimal;
import java.util.Map;

public interface FacadeService {
    void registerPerson(String name, String email, BigDecimal money, Long parentId, BinaryPerson personToPutItOn, boolean preferredDirection);
    Map<SubscriptionPlan, BigDecimal> monthlyIncome(RegistrationPerson registrationPerson);

}
