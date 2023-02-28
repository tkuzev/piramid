package com.example.piramidadjii.binaryTreeModule.entities;

import com.example.piramidadjii.baseEntities.BaseEntity;
import com.example.piramidadjii.registrationTreeModule.enums.OperationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class BinaryTransaction extends BaseEntity {

    @NotNull(message = "add binary person")
    @ManyToOne(fetch = FetchType.LAZY)
    private BinaryPerson binaryPerson;

    @NotNull(message = "price cannot be null")
    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;
}
