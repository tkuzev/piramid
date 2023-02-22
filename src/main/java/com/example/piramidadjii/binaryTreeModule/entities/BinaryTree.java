package com.example.piramidadjii.binaryTreeModule.entities;

import com.example.piramidadjii.baseEntities.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BinaryTree extends Person {

    private BinaryTree leftChild;
    private BinaryTree rightChild;
    private BigDecimal leftContainer;
    private BigDecimal rightContainer;


}
