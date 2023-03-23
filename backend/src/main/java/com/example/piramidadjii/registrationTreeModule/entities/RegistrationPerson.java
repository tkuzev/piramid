package com.example.piramidadjii.registrationTreeModule.entities;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.baseModule.baseEntites.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;

@Entity
@Table(name = "registration_person")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class RegistrationPerson extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

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

    @ManyToOne
    private Role role;

    public RegistrationPerson(String email, String password, Collection<SimpleGrantedAuthority> simpleGrantedAuthorities) {

    }
}
