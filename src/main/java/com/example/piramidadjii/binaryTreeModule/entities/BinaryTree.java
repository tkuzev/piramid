package com.example.piramidadjii.binaryTreeModule.entities;

import com.example.piramidadjii.baseEntities.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "binary_tree")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BinaryTree extends Person {

    @OneToOne
    private BinaryTree leftChild;
    @OneToOne
    private BinaryTree rightChild;
    private BigDecimal leftContainer;
    private BigDecimal rightContainer;


}
