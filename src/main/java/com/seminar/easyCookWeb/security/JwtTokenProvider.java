package com.seminar.easyCookWeb.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.seminar.easyCookWeb.config.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    private final UserDetailService userDetailService;
    private JwtConfig config;

    @Autowired
    public JwtTokenProvider(UserDetailService userDetailService){
        this.userDetailService = userDetailService;
    }

    @Autowired
    public void setJwtConfig(JwtConfig config){
        this.config = config;
    }

    public String createToken(String username, List<String> roles) {
        return JWT.create()
                .withSubject(username)
                .withAudience(roles.toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + config.getExpiration()))
                .sign(Algorithm.HMAC512(config.getSecret()));
    }

    public String resolveToken(HttpServletRequest req) {
        String header = req.getHeader(config.getHeader());
        if (header != null && header.startsWith(config.getPrefix())) return header;
        return null;
    }

    public String getUsername(String token) {
        return JWT.require(Algorithm.HMAC512(config.getSecret().getBytes()))
                .build()
                .verify(token.replace(config.getPrefix(), ""))
                .getSubject();
    }


    public boolean isValidatedToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(config.getSecret().getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
            verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return false;
        }
        return true;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername((getUsername(token)));
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }

}
