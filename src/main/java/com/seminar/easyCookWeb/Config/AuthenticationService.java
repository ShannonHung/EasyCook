package com.seminar.easyCookWeb.Config;

import com.seminar.easyCookWeb.Repository.MemberRepository;
import com.seminar.easyCookWeb.entity.app_user.Member;
import com.seminar.easyCookWeb.entity.app_user.UserAuthority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                return new UsernamePasswordAuthenticationToken(username, password, convertToSimpleAuthorities(user.get().getAuthorities()));
            }
        }
        return null;
    }
    private List<SimpleGrantedAuthority> convertToSimpleAuthorities(List<UserAuthority> authorities) {
        return authorities.stream()
                .map(auth -> new SimpleGrantedAuthority(auth.name()))
                .collect(Collectors.toList());
    }
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
