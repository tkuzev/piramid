package com.example.piramidadjii.registrationTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface RegistrationTreeService {

    void registerPerson(RegistrationTree registrationTree);

}
