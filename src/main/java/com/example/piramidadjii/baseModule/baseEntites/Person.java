package com.example.piramidadjii.baseModule.baseEntites;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Person{

    @NotNull(message = "Name cannot be empty")
    @Size(min = 3,max = 20)
    private String name;

    @OneToOne
    private BankAccount bankAccount;

}
