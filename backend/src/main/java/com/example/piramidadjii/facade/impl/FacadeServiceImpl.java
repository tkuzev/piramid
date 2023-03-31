package com.example.piramidadjii.facade.impl;

import com.example.piramidadjii.bankAccountModule.services.BankService;
import com.example.piramidadjii.binaryTreeModule.dtos.BinaryRegistrationDTO;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.facade.FacadeService;
import com.example.piramidadjii.facade.dto.BinaryDTO;
import com.example.piramidadjii.facade.dto.EditPersonDTO;
import com.example.piramidadjii.orchestraModule.OrchestraService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.services.SubscriptionPlanService;
import com.example.piramidadjii.registrationTreeModule.services.TransactionService;
import com.example.piramidadjii.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class FacadeServiceImpl implements FacadeService {
    @Autowired
    private OrchestraService orchestraService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private BankService bankService;
    @Autowired
    SubscriptionPlanService subscriptionPlanService;
    @Autowired
    BinaryRegistrationService binaryRegistrationService;
    @Autowired
    JWTGenerator jwtGenerator;

    @Override
    public void registerPerson(RegistrationPerson registrationPerson, BigDecimal money) {
        orchestraService.registerPerson(registrationPerson, money);
    }

    @Override
    public Map<String, BigDecimal> monthlyIncome(Long id) {
        return transactionService.monthlyIncome(id);
    }

    @Override
    public void deposit(Long id, BigDecimal money) {
        bankService.deposit(id, money);
    }

    @Override
    public void withdraw(Long id, BigDecimal money) {
        bankService.withdraw(id, money);
    }

    @Override
    public void createTransaction(RegistrationPerson registrationPerson, BigDecimal price) {
        transactionService.createTransaction(registrationPerson, price);
    }

    @Override
    public void editProfile(EditPersonDTO editPersonDTO) {
        orchestraService.editProfile(editPersonDTO);
    }

    @Override
    public List<BigDecimal> wallet(Long registrationPersonId) {
        return transactionService.wallet(registrationPersonId);
    }

    @Override
    public String getEmailFromJWT(String token) {
        return jwtGenerator.getEmailFromJWT(token);
    }

    @Override
    public List<BinaryDTO> getTree(BinaryPerson binaryPerson) {
        return binaryRegistrationService.getTree(binaryPerson);
    }
}
