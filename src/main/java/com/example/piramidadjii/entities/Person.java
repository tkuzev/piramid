package com.example.piramidadjii.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
public class Person extends BaseEntity{

    private String name;

    @ManyToOne
    private SubscriptionPlan subscriptionPlan;
    @ManyToOne
    private Person parent;

    private BigDecimal balance;

}
