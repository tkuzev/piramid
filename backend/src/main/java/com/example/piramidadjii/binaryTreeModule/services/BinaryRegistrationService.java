package com.example.piramidadjii.binaryTreeModule.services;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;

import java.util.Map;

public interface BinaryRegistrationService {
    void registerNewBinaryPerson(Long personId, Long personToPutItOnId, boolean preferredDirection);

    void sendBinaryRegistrationEmail(RegistrationPerson registrationPerson, Long parentId);

    Map<BinaryPerson, Boolean> getTree(Long binaryPersonId);

    BinaryPerson getBinaryById(Long id);
}
