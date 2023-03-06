package com.example.piramidadjii.orchestraModule;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;

public interface OrchestraService {
    BinaryPerson registerNewPerson(RegistrationPerson person, boolean preferredDirection);

}
