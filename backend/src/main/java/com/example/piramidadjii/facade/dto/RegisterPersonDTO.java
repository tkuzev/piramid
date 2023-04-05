package com.example.piramidadjii.facade.dto;

import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RegisterPersonDTO {

    Long id;
    String name;
    String email;
    Long parentId;
    BigDecimal money;
    String password;
    boolean isSubscriptionEnabled;
    SubscriptionPlan subscriptionPlan;

}
