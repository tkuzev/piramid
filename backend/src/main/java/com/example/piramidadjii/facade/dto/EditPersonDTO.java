package com.example.piramidadjii.facade.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditPersonDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private boolean isSubscriptionEnabled;

    //TODO: da smenim id sus subscprition plan kato imame frontend
    private Long subscriptionPlanId;
}
