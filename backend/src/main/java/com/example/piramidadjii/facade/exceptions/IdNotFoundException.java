package com.example.piramidadjii.facade.exceptions;

import lombok.Getter;

@Getter
public class IdNotFoundException extends RuntimeException{
    private final Long personId;

    public IdNotFoundException(Long personId){
        this.personId=personId;
    }


}
