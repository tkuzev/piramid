package com.example.piramidadjii.baseEntities;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    @NotNull(message = "Add bank account")
    @ManyToOne
    private BankAccount bankAccount;

}
