package com.example.piramidadjii.binaryTreeModule.services;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;

import java.util.List;

public interface BinaryRegistrationService {
    void registerNewBinaryPerson(RegistrationPerson person, Long personToPutItOnId, boolean preferredDirection);

    void sendBinaryRegistrationEmail(RegistrationPerson registrationPerson, Long parentId);

    List<BinaryPerson> getTree(BinaryPerson binaryPerson);
}
