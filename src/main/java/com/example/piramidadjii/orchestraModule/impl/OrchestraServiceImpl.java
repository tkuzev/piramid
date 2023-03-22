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
    public void registerPerson(RegistrationPerson registrationPerson, BigDecimal money) {
        registrationPersonService.registerPerson(registrationPerson, money);
        if (registrationPerson.getSubscriptionPlan().isEligibleForBinary()) {
            binaryRegistrationService.sendBinaryRegistrationEmail(registrationPerson, registrationPerson.getId());
        }
    }
}
