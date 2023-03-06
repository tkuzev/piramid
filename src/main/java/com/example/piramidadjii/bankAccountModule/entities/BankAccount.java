package com.example.piramidadjii.bankAccountModule.entities;

import com.example.piramidadjii.baseModule.baseEntites.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount extends BaseEntity {


    @NotNull(message = "balance cannot be null")
    @Column(nullable = false)
    private BigDecimal balance;

}
