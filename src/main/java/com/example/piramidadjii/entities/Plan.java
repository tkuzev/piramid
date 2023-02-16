package com.example.piramidadjii.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "subscription_plan")
@NoArgsConstructor
@Getter
@Setter
public class Plan extends BaseEntity{

    private String name;

    private Integer levels;

    private BigDecimal percent;

    private BigDecimal registrationFee;
}
