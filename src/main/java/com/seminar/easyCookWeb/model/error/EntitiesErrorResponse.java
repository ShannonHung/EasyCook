package com.seminar.easyCookWeb.model.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class EntitiesErrorResponse extends ErrorResponse{
    List<EntityErrorResponse> entityErrorResponses = new ArrayList<>();
    public EntitiesErrorResponse(HttpStatus status, String message, List<EntityErrorResponse> entityErrorResponses, Throwable ex){
        super(status, message, ex);
        this.entityErrorResponses = entityErrorResponses;
    }
}
