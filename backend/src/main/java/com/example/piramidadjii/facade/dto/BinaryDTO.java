package com.example.piramidadjii.facade.dto;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinaryDTO {
    private Long id;

    private BinaryPerson leftChild;
    private BinaryPerson rightChild;
    private BinaryPerson parent;
    private String name;
    private String email;
}
