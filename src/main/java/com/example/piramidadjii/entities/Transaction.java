package com.example.piramidadjii.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
public class Transaction extends BaseEntity{

    private Long personId;
    @Column(nullable = false)
    private BigDecimal percent;
    @Column(nullable = false)
    private BigDecimal price;
}
