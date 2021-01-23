package com.seminar.easyCookWeb.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class JwtConfig {
    @Value("${security.jwt.url:/api/auth/login}")
    private String url;
    @Value("${security.jwt.header:Authorization}")
    private String header;
    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;
    @Value("${security.jwt.expiration:#{24*60*60*1000}}")
    private long expiration;
    @Value("${security.jwt.secret}")
    private String secret;
}
