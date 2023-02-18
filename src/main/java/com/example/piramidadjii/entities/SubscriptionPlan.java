package com.example.piramidadjii.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "subscription_plan")
@NoArgsConstructor
@Getter
@Setter
public class SubscriptionPlan extends BaseEntity{
    private String name;

    private String percents;

    private BigDecimal registrationFee;

}
