package com.adrianbcodes.timemanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AlreadyDeletedException extends RuntimeException{
    public AlreadyDeletedException(String message){
        super(message);
    }
}
