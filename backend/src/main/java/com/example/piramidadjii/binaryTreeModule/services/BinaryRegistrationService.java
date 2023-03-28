package com.example.piramidadjii.binaryTreeModule.services;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;

public interface BinaryRegistrationService {
    void registerNewBinaryPerson(RegistrationPerson person, Long personToPutItOnId, boolean preferredDirection);

    void sendBinaryRegistrationEmail(RegistrationPerson registrationPerson, Long parentId);
}
