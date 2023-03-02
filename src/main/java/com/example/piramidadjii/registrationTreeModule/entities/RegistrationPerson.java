package com.example.piramidadjii.registrationTreeModule.entities;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.baseEntities.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "registration_person")
@Getter
@Setter
@NoArgsConstructor
public class RegistrationPerson extends Person {

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
