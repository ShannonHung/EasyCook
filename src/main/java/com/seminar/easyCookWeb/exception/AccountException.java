package com.seminar.easyCookWeb.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@NoArgsConstructor
public class AccountException extends RuntimeException{
    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Object... args){
        super(MessageFormat.format(message, args));
    }

}
