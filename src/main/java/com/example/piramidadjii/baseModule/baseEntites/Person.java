package com.example.piramidadjii.baseModule.baseEntites;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class Person extends BaseEntity {

    @NotNull(message = "Name cannot be empty")
    @Size(min = 3,max = 20)
    private String name;

    @NotNull(message = "Email cannot be null")
    @Column(unique = true)
    @Email
    private String email;


    @OneToOne
    private BankAccount bankAccount;

}
