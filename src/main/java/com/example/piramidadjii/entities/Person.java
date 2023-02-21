package com.example.piramidadjii.entities;

import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class Person extends BaseEntity {

    private String name;

    @ManyToOne
    private SubscriptionPlan subscriptionPlan;

    private BigDecimal balance;

}
