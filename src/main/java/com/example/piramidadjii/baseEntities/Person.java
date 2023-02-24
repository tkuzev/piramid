package com.example.piramidadjii.baseEntities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
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

    private BigDecimal balance;

}
