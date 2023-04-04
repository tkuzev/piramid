package com.example.piramidadjii.orchestraModule;

import com.example.piramidadjii.facade.dto.EditPersonDTO;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

import java.math.BigDecimal;

public interface OrchestraService {
    void registerPerson(RegistrationPerson registrationPerson, BigDecimal money);

    void editProfile(RegistrationPerson registrationPerson, SubscriptionPlan subscriptionPlan);

    void createTransaction(RegistrationPerson person, BigDecimal money);
}
