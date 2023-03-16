package com.example.piramidadjii.facade.dto;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RegisterPersonDTO {

    String name;
    String email;
    BigDecimal money;
    Long parentId;
    Long personToPutItOn;
    boolean preferredDirection;

}
