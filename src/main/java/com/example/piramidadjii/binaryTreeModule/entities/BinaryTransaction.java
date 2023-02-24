package com.example.piramidadjii.binaryTreeModule.entities;

import com.example.piramidadjii.baseEntities.BaseEntity;
import com.example.piramidadjii.registrationTreeModule.enums.OperationType;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private BinaryTree binaryTree;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;
}
