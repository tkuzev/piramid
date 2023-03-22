package com.example.piramidadjii.facade.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DepositDTO {
    private Long id;
    private BigDecimal money;
}
