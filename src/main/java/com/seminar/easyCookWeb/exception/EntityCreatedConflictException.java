package com.seminar.easyCookWeb.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityCreatedConflictException extends RuntimeException{
    public EntityCreatedConflictException(String message) {
        super(message);
    }
}
