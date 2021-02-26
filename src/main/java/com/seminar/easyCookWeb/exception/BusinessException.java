package com.seminar.easyCookWeb.exception;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@NoArgsConstructor
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Object... args){
        super(MessageFormat.format(message, args));
    }
}

