package com.example.piramidadjii.registrationTreeModule.entities;

import com.example.piramidadjii.baseEntities.BaseEntity;
import com.example.piramidadjii.registrationTreeModule.enums.OperationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "registration_transaction")
@Getter
@Setter
@NoArgsConstructor
public class RegistrationTransaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private RegistrationPerson registrationPerson;

    @NotNull(message = "percent cannot be null")
    @Column(nullable = false)
    private Long percent;

    @NotNull(message = "add price")
    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;
}
