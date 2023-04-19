package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.facade.dto.EditPersonDTO;
import com.example.piramidadjii.facade.exceptions.EmailNotFoundException;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;

import java.math.BigDecimal;


public interface RegistrationPersonService {

    RegistrationPerson registerPerson(Long parentId,String name,String email,String password, BigDecimal money);

    void setSubscription(RegistrationPerson registrationPerson, long id);

    void editPerson(RegistrationPerson registrationPerson);
    RegistrationPerson displayPersonDetails(String email);

    RegistrationPerson getRegistrationPersonByEmail(String email);

    RegistrationPerson getRegistrationPersonById(Long id);
}
