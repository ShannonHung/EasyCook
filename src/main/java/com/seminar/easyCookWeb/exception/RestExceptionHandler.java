package com.seminar.easyCookWeb.exception;

import com.seminar.easyCookWeb.model.error.EntitiesErrorResponse;
import com.seminar.easyCookWeb.model.error.EntityErrorResponse;
import com.seminar.easyCookWeb.model.error.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(AccountException.class)
    protected ResponseEntity<Object> handleBusiness(AccountException ex) {
        return buildResponseEntity(new ErrorResponse(BAD_REQUEST, ex.getMessage(), ex), ex);
    }

    /**
     * 如果create的時候發生錯誤
     * @param ex
     * @return
     */
    @ExceptionHandler({EntitiesErrorException.class})
    protected ResponseEntity<Object> handleEntitiesNotFound(EntitiesErrorException ex) {
        List<EntityErrorResponse> errors = ex.getFieldExceptionList().stream()
                .map(it -> new EntityErrorResponse(it.getField(), it.getMessage()))
                .collect(Collectors.toList());
        return buildResponseEntity(new EntitiesErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), errors, ex), ex);
    }



    //如果發生500 Status錯誤
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request.";
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, error, ex), ex);
    }


    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorResponse apiError = new ErrorResponse(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError, ex);
    }

    //負責處理Validation也就是@NotNull, NotEmpty, NotBlank的部分驗證
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        List<EntityErrorResponse> errors = ex.getConstraintViolations().stream()
                .map(it -> new EntityErrorResponse(it.getPropertyPath().toString(), it.getMessage()))
                .collect(Collectors.toList());
        return buildResponseEntity(new EntitiesErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), errors, ex), ex);
    }

    /**
     * 處理刪除的錯誤
     * @param ex
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Object> handleBusiness(BusinessException ex) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex), ex);
    }


    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex) {
        ErrorResponse apiError = new ErrorResponse(FORBIDDEN, "NEED AUTHORIZATION", ex);
        return buildResponseEntity(apiError, ex);
    }

    /**
     * 如果發生資料庫已經存在該帳戶 發出此exception
     * @param ex the ConflictException
     * @return
     */
    @ExceptionHandler(EntityCreatedConflictException.class)
    protected ResponseEntity<Object> handleEntityCreatedConflict(EntityCreatedConflictException ex) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), ex), ex);
    }

    /**
     * Handle HttpMessageNotWritableException.controller将返回参数json化后，返回给前端的时候，报异常
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        return buildResponseEntity(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, error, ex), ex);
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse apiError = new ErrorResponse(BAD_REQUEST);
        apiError.setMessage(String.format("Could not find the %s method for URL %s", "ACCOUNT OR PASSWORD NOT CORRECT", ex.getRequestURL()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError, ex);
    }

    /**
     * Handle javax.persistence.EntityNotFoundException
     */
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND, ex), ex);
    }




    private ResponseEntity<Object> buildResponseEntity(ErrorResponse apiError, Exception ex) {
        log.error("The ErrorResponse is {} ", apiError.toString(), ex);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
