package com.seminar.easyCookWeb.model.error;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EntityErrorResponse {

    private String field;

    private String message;

    public EntityErrorResponse(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
