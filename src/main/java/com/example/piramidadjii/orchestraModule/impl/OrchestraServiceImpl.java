package com.example.piramidadjii.orchestraModule.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.configModule.ConfigurationService;
import com.example.piramidadjii.orchestraModule.OrchestraService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrchestraServiceImpl implements OrchestraService {

    @Autowired
    private BinaryRegistrationService binaryRegistrationService;
    @Autowired
    private RegistrationPersonService registrationPersonService;


    @Override
    public void registerPerson(String name, String email, BigDecimal money, Long parentId, long personToPutItOnId, boolean preferredDirection, SubscriptionPlan subscriptionPlan) {
        RegistrationPerson registrationPerson = registrationPersonService.registerPerson(name,email ,money, parentId,subscriptionPlan);
        binaryRegistrationService.registerNewBinaryPerson(registrationPerson, personToPutItOnId, preferredDirection);
    }

    @Override
    public void registerPerson(String name, String email, BigDecimal money, Long parentId, SubscriptionPlan subscriptionPlan) {
        registrationPersonService.registerPerson(name,email ,money, parentId,subscriptionPlan);
    }
}
