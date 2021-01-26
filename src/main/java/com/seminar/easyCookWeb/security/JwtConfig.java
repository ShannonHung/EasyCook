package com.seminar.easyCookWeb.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class JwtConfig {
    @Value("${security.jwt.url:/login}")
    private String url;
    @Value("${security.jwt.header:Authorization}")
    private String header;
    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;
    @Value("${security.jwt.expiration:#{60*24}}")
    private int expiration;
    @Value("${security.jwt.secret}")
    private String secret;
}
