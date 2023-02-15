package com.example.piramidadjii.dtos;

import com.example.piramidadjii.entities.Plan;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO {
    private String name;

    private Plan subscriptionPlan;

    private Long parentId;
}
