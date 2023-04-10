package com.example.piramidadjii.facade.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SubscriptionPlanDTO {
    String name;
    String percents;
    BigDecimal registrationFee;
    boolean isEligibleForBinary;

}
