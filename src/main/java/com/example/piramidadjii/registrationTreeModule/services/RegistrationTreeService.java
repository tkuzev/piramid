package com.example.piramidadjii.registrationTreeModule.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface RegistrationTreeService {

    void registerPerson(String name, String email, BigDecimal balance, Long parentId);

}
