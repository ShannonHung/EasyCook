package com.seminar.easyCookWeb.Service;

import com.seminar.easyCookWeb.Repository.MemberRepository;
import com.seminar.easyCookWeb.entity.app_user.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class AuthenticationService implements AuthenticationProvider {
    MemberRepository memberRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AuthenticationService(MemberRepository memberRepository){
        this.memberRepository= memberRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        log.warn("[AuthenticationService] -> username: "+username +" password:" + password);
        Optional<Member> user = memberRepository.findByAccount(username);
        if(user!=null){
            log.warn("[AuthenticationService] -> equal? user's password->"+ passwordEncoder.matches(password, user.get().getPassword()));
            if(passwordEncoder.matches(password, user.get().getPassword())){
                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
