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
    private RegistrationPersonRepository registrationPersonRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private DistributeMoneyService distributeMoneyService;
    @Autowired
    private BinaryPersonRepository binaryPersonRepository;
    @Autowired
    private SubscriptionPlanService subscriptionPlanService;


    @Override
    public void registerPerson(Long parentId,String name,String email,String password, BigDecimal money) {
        if (registrationPersonRepository.existsByEmail(email)){
            throw new RuntimeException("Emaila trqq da e unique");
        }

        RegistrationPerson registrationPerson = registrationPersonService.registerPerson(parentId,name,email,password, money);

        if (registrationPerson.getSubscriptionPlan().isEligibleForBinary()) {
            binaryRegistrationService.sendBinaryRegistrationEmail(registrationPerson, registrationPerson.getId());
        }
    }

    @Override
    public void editProfile(RegistrationPerson registrationPerson) {
        registrationPersonService.editPerson(registrationPerson);
    }

    @Override
    public void createTransaction(Long personId, BigDecimal money) {
        transactionService.createTransaction(personId, money);
        if (binaryPersonRepository.existsById(personId)) {
            BinaryPerson binaryPerson = binaryPersonRepository.findById(personId).orElseThrow();
            distributeMoneyService.distributeMoney(binaryPerson, money);
        }
    }

    @Override
    public void upgradeSubscriptionPlan(Long id, SubscriptionPlan subscriptionPlan) {
        subscriptionPlanService.upgradeSubscriptionPlan(id,subscriptionPlan);
        RegistrationPerson registrationPerson = registrationPersonRepository.findById(id).orElseThrow();

        if (!registrationPerson.getSubscriptionPlan().isEligibleForBinary() && subscriptionPlan.isEligibleForBinary()){
            binaryRegistrationService.sendBinaryRegistrationEmail(registrationPerson, registrationPerson.getId());
        }

    }
}
