package com.seminar.easyCookWeb.exception;

import lombok.Getter;

import java.text.MessageFormat;

@Getter //for get field
public class EntityFieldException extends RuntimeException{
    private String field;
    public EntityFieldException(String field, String message) {
        super(MessageFormat.format("The field of the entity error: {0}. {1}", field, message));
        this.field = field;
    }
}
