package com.example.piramidadjii.facade;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.facade.dto.EditPersonDTO;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FacadeService {
    void registerPerson(Long parentId,String name,String email,String password, BigDecimal money);

    Map<String, BigDecimal> monthlyIncome(Long id);

    void deposit(Long id, BigDecimal money);

    void withdraw(Long id, BigDecimal money);

    void createTransaction(Long registrationPersonId, BigDecimal price);

    void editProfile(RegistrationPerson registrationPerson);

    List<BigDecimal> wallet(Long registrationPersonId);

    String getEmailFromJWT(String token);

    Map<BinaryPerson, Boolean> getTree(Long binaryPersonId);

    RegistrationPerson displayPersonDetails(String email);

    BinaryPerson getBinaryPersonById(Long id);

    RegistrationPerson getRegistrationPersonByEmail(String email);

    List<SubscriptionPlan> getAllSubscriptionPlans();

    RegistrationPerson getRegistrationPersonById(Long id);

    void registerNewBinaryPerson(Long childId, Long personToPutItOnId, Boolean preferredDirection);

    void upgradeSubscriptionPlan(Long id,SubscriptionPlan subscriptionPlan);
}
