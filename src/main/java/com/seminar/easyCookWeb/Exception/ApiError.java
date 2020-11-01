package com.seminar.easyCookWeb.Exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {
    //The status property holds the operation call status, 像是常見的403 Forbidden, 500 BadRequest
    private HttpStatus status;

    //timestamp property holds the date-time instance of when the error happened設定比較易懂的時間
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    //
    private String message;

    //The debugMessage property holds a system message describing the error in more detail
    private String debugMessage;

    // This is used for representing multiple errors in a single call.
    // An example would be validation errors in which multiple fields have failed the validation
    private List<ApiSubError> subErrors;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    ApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
}
