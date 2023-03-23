package com.example.piramidadjii.facade.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RegisterPersonDTO {

    String name;
    String email;
    Long parentId;
    BigDecimal money;
    String password;

}
