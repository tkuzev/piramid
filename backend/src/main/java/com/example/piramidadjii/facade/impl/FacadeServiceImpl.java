package com.example.piramidadjii.facade.impl;

import com.example.piramidadjii.bankAccountModule.services.BankService;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.facade.FacadeService;
import com.example.piramidadjii.facade.dto.EditPersonDTO;
import com.example.piramidadjii.orchestraModule.OrchestraService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.services.SubscriptionPlanService;
import com.example.piramidadjii.registrationTreeModule.services.TransactionService;
import com.example.piramidadjii.registrationTreeModule.services.impl.RegistrationPersonServiceImpl;
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
    RegistrationPersonServiceImpl registrationPersonService;
    @Autowired
    JWTGenerator jwtGenerator;

    @Override
    public void registerPerson(Long parentId,String name,String email,String password, BigDecimal money) {
        orchestraService.registerPerson(parentId,name,email,password, money);
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
    public void createTransaction(Long registrationPersonId, BigDecimal price) {
        orchestraService.createTransaction(registrationPersonId, price);
    }

    @Override
    public void editProfile(RegistrationPerson registrationPerson) {
        orchestraService.editProfile(registrationPerson);
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
    public Map<BinaryPerson, Boolean> getTree(Long binaryPersonId) {
        return binaryRegistrationService.getTree(binaryPersonId);
    }

    @Override
    public RegistrationPerson displayPersonDetails(String email) {
        return registrationPersonService.displayPersonDetails(email);
    }

    @Override
    public BinaryPerson getBinaryPersonById(Long id) {
        return binaryRegistrationService.getBinaryById(id);
    }

    @Override
    public RegistrationPerson getRegistrationPersonByEmail(String email) {
        return registrationPersonService.getRegistrationPersonByEmail(email);
    }

    @Override
    public List<SubscriptionPlan> getAllSubscriptionPlans() {
        return subscriptionPlanService.listOfAllSubscriptionPlans();
    }

    @Override
    public RegistrationPerson getRegistrationPersonById(Long id) {
       return registrationPersonService.getRegistrationPersonById(id);
    }

    @Override
    public void registerNewBinaryPerson(Long childId, Long personToPutItOnId, Boolean preferredDirection) {
        binaryRegistrationService.registerNewBinaryPerson(childId,personToPutItOnId,preferredDirection);
    }

    @Override
    public void upgradeSubscriptionPlan(Long id, SubscriptionPlan subscriptionPlan) {
        orchestraService.upgradeSubscriptionPlan(id,subscriptionPlan);
    }
}
