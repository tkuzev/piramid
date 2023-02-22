package com.example.piramidadjii.registrationTreeModule.entities;

import com.example.piramidadjii.baseEntities.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "registration_tree")
@Getter
@Setter
@NoArgsConstructor
public class RegistrationTree extends Person {
    @ManyToOne
    private RegistrationTree parent;
    @ManyToOne
    private SubscriptionPlan subscriptionPlan;
    private BigDecimal balance;

    private LocalDate subscriptionExpirationDate;

    private boolean isSubscriptionEnabled;
}
