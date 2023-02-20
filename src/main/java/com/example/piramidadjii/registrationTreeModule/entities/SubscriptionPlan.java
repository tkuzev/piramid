package com.example.piramidadjii.registrationTreeModule.entities;

import com.example.piramidadjii.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private String name;

    private String percents;

    private BigDecimal registrationFee;
}
