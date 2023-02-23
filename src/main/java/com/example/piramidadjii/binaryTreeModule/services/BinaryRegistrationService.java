package com.example.piramidadjii.binaryTreeModule.services;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryTree;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import org.springframework.stereotype.Service;

public interface BinaryRegistrationService {
    BinaryTree registerNewPerson(RegistrationTree person, boolean preferredDirection);
}
