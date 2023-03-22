package com.example.piramidadjii.facade.dto;

import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditPersonDTO {
    private String name;
    private String email;
    private boolean isSubscriptionEnabled;
    private SubscriptionPlan subscriptionPlan;
}
