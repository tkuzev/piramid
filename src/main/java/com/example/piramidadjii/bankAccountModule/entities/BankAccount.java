package com.example.piramidadjii.bankAccountModule.entities;

import com.example.piramidadjii.baseModule.baseEntites.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount extends BaseEntity {

    @NotNull(message = "balance cannot be null")
    @Column(nullable = false)
    private BigDecimal balance;

}
