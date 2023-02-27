package com.example.piramidadjii.bankAccountModule.entities;

import com.example.piramidadjii.baseEntities.BaseEntity;
import com.example.piramidadjii.baseEntities.Person;
import jakarta.persistence.*;
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

    private String email;
    private BigDecimal balance;

}
