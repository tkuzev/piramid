package com.example.piramidadjii.registrationTreeModule.entities;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.baseEntities.Person;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToOne
    private BankAccount bankAccount;

    private LocalDate subscriptionExpirationDate;

    private Boolean isSubscriptionEnabled;
}
