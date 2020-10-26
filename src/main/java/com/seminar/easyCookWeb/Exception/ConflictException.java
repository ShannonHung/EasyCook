package com.seminar.easyCookWeb.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException{
    public ConflictException(){super("accountName was conflicted!!");}
    public ConflictException(String message){
        super(message);
        System.out.println("Here was called: " + message);
    }
}
