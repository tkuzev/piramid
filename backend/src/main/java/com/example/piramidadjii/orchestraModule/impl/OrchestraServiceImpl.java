package com.example.piramidadjii.orchestraModule.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.binaryTreeModule.services.DistributeMoneyService;
import com.example.piramidadjii.facade.dto.EditPersonDTO;
import com.example.piramidadjii.orchestraModule.OrchestraService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationPersonService;
import com.example.piramidadjii.registrationTreeModule.services.SubscriptionPlanService;
import com.example.piramidadjii.registrationTreeModule.services.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
public class OrchestraServiceImpl implements OrchestraService {

    @Autowired
    private BinaryRegistrationService binaryRegistrationService;
    @Autowired
    private RegistrationPersonService registrationPersonService;
    @Autowired
    private SubscriptionPlanService subscriptionPlanService;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private DistributeMoneyService distributeMoneyService;
    @Autowired
    private BinaryPersonRepository binaryPersonRepository;


    @Override
    public void registerPerson(RegistrationPerson registrationPerson, BigDecimal money) {
        if (registrationPersonRepository.existsByEmail(registrationPerson.getEmail())) {
            throw new RuntimeException("Emaila trqq da e unique");
        }

        RegistrationPerson registrationPerson1 = registrationPersonService.registerPerson(registrationPerson, money);
        if (registrationPerson.getSubscriptionPlan().isEligibleForBinary()) {
            binaryRegistrationService.sendBinaryRegistrationEmail(registrationPerson1, registrationPerson1.getId());
        }
    }

    @Override
    public void editProfile(RegistrationPerson registrationPerson) {
        registrationPersonService.editPerson(registrationPerson);
    }

    @Override
    public void createTransaction(RegistrationPerson person, BigDecimal money) {
        transactionService.createTransaction(person, money);
        if (binaryPersonRepository.existsById(person.getId())) {
            BinaryPerson binaryPerson = binaryPersonRepository.findById(person.getId()).orElseThrow();
            distributeMoneyService.distributeMoney(binaryPerson, money);
        }
    }
}
