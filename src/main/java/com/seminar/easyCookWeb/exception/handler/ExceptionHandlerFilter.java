package com.seminar.easyCookWeb.exception.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminar.easyCookWeb.model.error.ErrorResponse;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private ObjectMapper mapper;

    public ExceptionHandlerFilter() {
        this.mapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException e) {
            this.writeResponse(response, new ErrorResponse(HttpStatus.BAD_REQUEST, "TokenResponse error expired", e));
        } catch (JWTVerificationException e) {
            this.writeResponse(response, new ErrorResponse(HttpStatus.BAD_REQUEST, "Invalid token", e));
        } catch (Exception e) {
            this.writeResponse(response, new ErrorResponse(HttpStatus.BAD_REQUEST, "Unexpected error", e));
        }
    }

    //簡單來說就是try ... catch
    @SneakyThrows(IOException.class)
    private void writeResponse(HttpServletResponse response, ErrorResponse errorResponse) {
        response.setStatus(errorResponse.getStatus().value());
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
