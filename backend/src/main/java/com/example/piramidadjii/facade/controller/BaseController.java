package com.example.piramidadjii.facade.controller;

import com.example.piramidadjii.facade.exceptions.IdNotFoundException;
import com.example.piramidadjii.facade.exceptions.InvalidMoneyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class BaseController {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({IdNotFoundException.class})
    public void idNotFoundResponse(IdNotFoundException exception){
        System.out.println("person with id: "+exception.getPersonId()+" not found");
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({InvalidMoneyException.class})
    public void idNotFoundResponse(){
        System.out.println("Invalid money!");
    }
}
