package com.example.piramidadjii.facade.dto;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpgradeSubscriptionPlanDTO {
    private Long registrationPersonId;
    private SubscriptionPlan subscriptionPlan;
}
