package com.example.piramidadjii.facade.dto;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinaryPersonDTO {

    RegistrationPerson parent;

    public boolean isPreferredDirection() {
        return preferredDirection;
    }

    Long ChildId;
    Long binaryPersonToPutItOnId;
    boolean preferredDirection;
}
