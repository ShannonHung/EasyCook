package com.seminar.easyCookWeb.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminar.easyCookWeb.model.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = Map.of("error", "請先登入才能進行此操作");
        String error = mapper.writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(response.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.write(error);
        writer.flush();
        writer.close();

//        StringWriter errors = new StringWriter();
//        authException.printStackTrace(new PrintWriter(errors));
//        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, errors.toString(), authException);
//        response.setStatus(errorResponse.getStatus().value());
//        OutputStream out = response.getOutputStream();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValue(out, errorResponse);
//        out.flush();

    }

}
