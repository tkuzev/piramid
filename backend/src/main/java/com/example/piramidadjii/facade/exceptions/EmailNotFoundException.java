package com.example.piramidadjii.facade.exceptions;

import lombok.Getter;

@Getter
public class EmailNotFoundException extends RuntimeException{
    private final String message;
    public EmailNotFoundException() {
        this.message="Email was not found!";
    }
}
