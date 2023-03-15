package com.example.piramidadjii.facade.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.facade.FacadeService;
import com.example.piramidadjii.orchestraModule.OrchestraService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class FacadeServiceImpl implements FacadeService {
    @Autowired
    private OrchestraService orchestraService;
    @Autowired
    private TransactionService transactionService;


    @Override
    public void registerPerson(String name, String email, BigDecimal money, Long parentId, BinaryPerson personToPutItOn, boolean preferredDirection) {
        orchestraService.registerPerson(name, email, money, parentId, personToPutItOn, preferredDirection);
    }

    @Override
    public Map<SubscriptionPlan, BigDecimal> monthlyIncome(RegistrationPerson registrationPerson) {
        return transactionService.monthlyIncome(registrationPerson);
    }
}
