package com.example.piramidadjii.facade.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinaryDTO {
    private Long id;

    private Long leftChildId;
    private Long rightChildId;
    private Long parentId;
    private String name;
    private String email;

    private Boolean direction;
}
