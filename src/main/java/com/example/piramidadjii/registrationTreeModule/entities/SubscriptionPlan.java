package com.example.piramidadjii.registrationTreeModule.entities;

import com.example.piramidadjii.baseModule.baseEntites.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "subscription_plan")
@NoArgsConstructor
@Getter
@Setter
public class SubscriptionPlan extends BaseEntity {

    @NotNull(message = "name cannot be null")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "percents cannot be null")
    @Positive
    @Column(nullable = false)
    private String percents;

    @NotNull(message = "registrationFee cannot be null")
    @Positive
    @Column(nullable = false)
    private BigDecimal registrationFee;

    @NotNull(message = "isEligibleForBinary cannot be null")
    @Column(nullable = false)
    private boolean isEligibleForBinary;
}
