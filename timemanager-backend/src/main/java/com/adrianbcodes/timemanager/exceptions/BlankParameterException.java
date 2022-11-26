package com.adrianbcodes.timemanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BlankParameterException extends RuntimeException{
    public BlankParameterException(String message){
        super(message);
    }
}
