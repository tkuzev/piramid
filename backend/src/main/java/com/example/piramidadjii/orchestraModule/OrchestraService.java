package com.example.piramidadjii.orchestraModule;

import com.example.piramidadjii.facade.dto.EditPersonDTO;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

import java.math.BigDecimal;

public interface OrchestraService {
    void registerPerson(Long parentId,String name,String email,String password, BigDecimal money);

    void editProfile(RegistrationPerson registrationPerson);

    void createTransaction(Long personId, BigDecimal money);

    void upgradeSubscriptionPlan(Long id,SubscriptionPlan subscriptionPlan);
}
