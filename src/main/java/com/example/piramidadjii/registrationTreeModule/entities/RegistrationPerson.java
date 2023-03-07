package com.example.piramidadjii.registrationTreeModule.entities;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.baseModule.baseEntites.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "registration_person")
@Getter
@Setter
@NoArgsConstructor
public class RegistrationPerson extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RegistrationPerson parent;

    @ManyToOne
    private SubscriptionPlan subscriptionPlan;

    @NotNull(message = "bank account is null")
    @OneToOne
    @Column(nullable = false)
    private BankAccount bankAccount;

    private LocalDate subscriptionExpirationDate;
    private Boolean isSubscriptionEnabled;

}
