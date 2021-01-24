package com.seminar.easyCookWeb.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminar.easyCookWeb.model.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

//身分是USER卻想存取ADMIN相關頁面
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, accessDeniedException.getMessage(), accessDeniedException);
//        response.setStatus(errorResponse.getStatus().value());
//        OutputStream out = response.getOutputStream();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValue(out, errorResponse);
//        out.flush();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = Map.of("error", "you don't have authorize to access this request");
        String error = mapper.writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(response.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.write(error);
        writer.flush();
        writer.close();
    }
}
