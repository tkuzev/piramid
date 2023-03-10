package com.example.piramidadjii.binaryTreeModule.entities;

import com.example.piramidadjii.baseModule.baseEntites.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "binary_person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BinaryPerson extends Person {

    @Id
    private Long id;

    @OneToOne
    private BinaryPerson leftChild;

    @OneToOne
    private BinaryPerson rightChild;

    private BigDecimal leftContainer;

    private BigDecimal rightContainer;

    @NotNull(message = "direction cannot be null")
    @Column(nullable = false)
    private boolean preferredDirection;

    @ManyToOne
    private BinaryPerson parent;

    public BinaryPerson(Long id, BigDecimal leftContainer, BigDecimal rightContainer, boolean preferredDirection) {
        this.id = id;
        this.leftContainer = leftContainer;
        this.rightContainer = rightContainer;
        this.preferredDirection = preferredDirection;
    }
}
