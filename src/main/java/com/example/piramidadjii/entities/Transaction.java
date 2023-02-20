package com.example.piramidadjii.entities;

import com.example.piramidadjii.entities.BaseEntity;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.enums.OperationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
public class Transaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private RegistrationTree registrationTree;
    @Column(nullable = false)
    private Long percent;
    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;
}
