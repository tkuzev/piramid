package com.example.piramidadjii.bankAccountModule.entities;

import com.example.piramidadjii.baseEntities.BaseEntity;
import com.example.piramidadjii.baseEntities.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount extends BaseEntity {

    @NotNull(message = "email cannot be null")
    @Email
    @Column(nullable = false)
    private String email;

    @NotNull(message = "balance cannot be null")
    @Column(nullable = false)
    private BigDecimal balance;

}
