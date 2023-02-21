package com.example.piramidadjii.binaryTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import org.springframework.stereotype.Service;

@Service
public interface BinaryRegistrationService {
    void registerNewPerson(RegistrationTree person);
}
