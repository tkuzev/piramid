package com.example.piramidadjii.orchestraModule.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.facade.dto.EditPersonDTO;
import com.example.piramidadjii.configModule.ConfigurationService;
import com.example.piramidadjii.orchestraModule.OrchestraService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationPersonService;
import com.example.piramidadjii.registrationTreeModule.services.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class OrchestraServiceImpl implements OrchestraService {

    @Autowired
    private BinaryRegistrationService binaryRegistrationService;
    @Autowired
    private RegistrationPersonService registrationPersonService;
    @Autowired
    private SubscriptionPlanService subscriptionPlanService;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;


    @Override
    public void registerPerson(RegistrationPerson registrationPerson, BigDecimal money) {
        registrationPersonService.registerPerson(registrationPerson, money);
        if (registrationPerson.getSubscriptionPlan().isEligibleForBinary()) {
            binaryRegistrationService.sendBinaryRegistrationEmail(registrationPerson, registrationPerson.getId());
        }
    }

    @Override
    public void editProfile(EditPersonDTO editPersonDTO) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(editPersonDTO.getSubscriptionPlanId()).orElseThrow();
        subscriptionPlanService.upgradeSubscriptionPlan(editPersonDTO.getId(),plan);
        registrationPersonService.editPerson(editPersonDTO);
    }
}
