package com.seminar.easyCookWeb.exception;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class EntitiesErrorException extends RuntimeException{
    private List<EntityFieldException> fieldExceptionList = new ArrayList<>();
    public EntitiesErrorException(String message){
        super(message);
    }

    public void add(EntityFieldException fieldException){
        this.fieldExceptionList.add(fieldException);
    }

    public boolean isErrorEmpty(){
        return fieldExceptionList.isEmpty();
    }

    public List<EntityFieldException> getFieldExceptionList() {
        return fieldExceptionList;
    }

}
