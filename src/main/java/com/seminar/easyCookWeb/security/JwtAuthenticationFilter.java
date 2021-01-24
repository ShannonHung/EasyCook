package com.seminar.easyCookWeb.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminar.easyCookWeb.config.JWTService;
import com.seminar.easyCookWeb.model.user.AuthRequest;
import com.seminar.easyCookWeb.pojo.appUser.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtConfig config;
    private final AuthenticationManager authenticationManager;
//    private final JwtTokenProvider jwtTokenProvider;
    private JWTService jwtService;
    @Autowired
    public JwtAuthenticationFilter(JwtConfig config, AuthenticationManager authenticationManager,JWTService jwtService) {
        this.config = config;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @SneakyThrows(IOException.class)
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        AuthRequest creds = new ObjectMapper()
                .readValue(req.getInputStream(), AuthRequest.class);
        System.out.println("JwtAuthenticationFilter=> " + creds.toString());
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.getAccount(),
                        creds.getPassword(),
                        Collections.emptyList())
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        System.out.println(auth.getPrincipal());
        User account = (User) auth.getPrincipal();
        System.out.println("successfulAuthentication => "+account.toString());
        String token = jwtService.generateToken(account.getAccount(), account.getPassword());
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print("{\"token\":\"" + config.getPrefix() + token + "\"}");
        out.flush();
    }


}
