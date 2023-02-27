package com.example.piramidadjii.baseEntities;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class Person extends BaseEntity {

    private String name;

    @Column(unique = true)
    private String email;

    @OneToOne
    private BankAccount bankAccount;

}
