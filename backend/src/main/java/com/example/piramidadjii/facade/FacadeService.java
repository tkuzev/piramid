package com.example.piramidadjii.facade;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.facade.dto.EditPersonDTO;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FacadeService {
    void registerPerson(RegistrationPerson registrationPerson, BigDecimal money);

    Map<String, BigDecimal> monthlyIncome(Long id);

    void deposit(Long id, BigDecimal money);

    void withdraw(Long id, BigDecimal money);

    void createTransaction(RegistrationPerson registrationPerson, BigDecimal price);

    void editProfile(RegistrationPerson registrationPerson);

    List<BigDecimal> wallet(Long registrationPersonId);

    String getEmailFromJWT(String token);

    Map<BinaryPerson, Boolean> getTree(BinaryPerson binaryPerson);

    RegistrationPerson displayPersonDetails(String email);
}
