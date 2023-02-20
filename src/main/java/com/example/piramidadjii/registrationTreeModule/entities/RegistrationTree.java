package com.example.piramidadjii.registrationTreeModule.entities;

import com.example.piramidadjii.entities.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "registration_tree")
@Getter
@Setter
@NoArgsConstructor
public class RegistrationTree extends Person {
    @ManyToOne
    private RegistrationTree registrationTree;
}
