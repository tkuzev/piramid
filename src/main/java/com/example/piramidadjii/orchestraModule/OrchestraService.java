package com.example.piramidadjii.orchestraModule;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;

import java.math.BigDecimal;

public interface OrchestraService {
    void registerPerson(RegistrationPerson registrationPerson, Long parentId, BigDecimal money);
}
