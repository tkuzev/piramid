package com.example.piramidadjii.binaryTreeModule.services;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;

public interface BinaryRegistrationService {

    BinaryPerson registerNewPerson(RegistrationPerson person, boolean preferredDirection);

    void changePreferredDirection(BinaryPerson binaryPerson, boolean direction);
}
