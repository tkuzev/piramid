package com.example.piramidadjii.orchestraModule.impl;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.configModule.ConfigurationService;
import com.example.piramidadjii.orchestraModule.OrchestraService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrchestraServiceImpl implements OrchestraService {

    @Autowired
    BinaryRegistrationService binaryRegistrationService;
    @Autowired
    ConfigurationService configurationService;
    @Override
    public void registerBinaryPerson(RegistrationPerson person, boolean preferredDirection) {
        if(configurationService.isEligable(person.getSubscriptionPlan())){
            binaryRegistrationService.registerNewBinaryPerson(person, preferredDirection);
        }

    }

}
