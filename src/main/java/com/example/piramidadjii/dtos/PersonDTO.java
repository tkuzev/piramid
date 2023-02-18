package com.example.piramidadjii.dtos;

import com.example.piramidadjii.entities.SubscriptionPlan;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO {
    private String name;

    private SubscriptionPlan subscriptionPlan;

    private Long parentId;
}
