package com.example.piramidadjii.orchestraModule.impl;
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
    BinaryRegistrationService binaryRegistrationService;
    @Autowired
    RegistrationPersonService registrationPersonService;
    @Autowired
    ConfigurationService configurationService;
    @Override
    public void registerPerson(String name, BigDecimal money, Long parentId, boolean preferredDirection) {
        RegistrationPerson registrationPerson = registrationPersonService.registerPerson(name, money, parentId);

        if(configurationService.isEligable(registrationPerson.getSubscriptionPlan())){
            binaryRegistrationService.registerNewBinaryPerson(registrationPerson, preferredDirection);
        }
    }
}
