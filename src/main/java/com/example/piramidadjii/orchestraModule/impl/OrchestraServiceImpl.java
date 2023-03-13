package com.example.piramidadjii.orchestraModule.impl;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.configModule.ConfigurationService;
import com.example.piramidadjii.orchestraModule.OrchestraService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
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
    @Autowired
    private ConfigurationService configurationService;
    @Override
    public void registerPerson(String name, BigDecimal money, Long parentId, BinaryPerson kovrata, boolean preferredDirection) {
        RegistrationPerson registrationPerson = registrationPersonService.registerPerson(name, money, parentId);
        if(configurationService.isEligable(registrationPerson.getSubscriptionPlan())){
            binaryRegistrationService.registerNewBinaryPerson(registrationPerson, kovrata, preferredDirection);
        }
    }
}
