package com.example.piramidadjii.facade.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SellDTO {
    private Long id;
    private BigDecimal price;
}
