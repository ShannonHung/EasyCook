package com.seminar.easyCookWeb.Config;

import com.seminar.easyCookWeb.Repository.Users.EmployeeRepository;
import com.seminar.easyCookWeb.Repository.Users.MemberRepository;
import com.seminar.easyCookWeb.Pojo.appUser.Role;
import com.seminar.easyCookWeb.Pojo.appUser.User;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    //這個Filter會解析出Token的資料，透過此查詢對應的使用者詳情
    //隨後提供驗證資料給Security機制表明這個request已經通過身分驗證
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //取得header裡面key=Authorization的value
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        //如果header裡面真的有東西
        if(authHeader != null){
            //只取得後面的部分
            String accessToken = authHeader.replace("Bearer ","");
            //解析token
            Map<String, Object> claims = jwtService.parseToken(accessToken);
            //因為是HashMap account裡面有所有Member的資料
            HashMap<String, Object> account = (HashMap<String, Object>) claims.get("UserInfo");

            Optional<User> user;
            if(memberRepository.findByAccount((String) account.get("account")).isPresent()){
                //從資料庫 取得Member的資料 get("account")取的帳號資料
                user = Optional.of(memberRepository.findByAccount((String) account.get("account")).get());
            }else if(employeeRepository.findByAccount((String) account.get("account")).isPresent()){
                user = Optional.of(employeeRepository.findByAccount((String) account.get("account")).get());
            }else{
                throw new JwtException("JWTFilter Exception?!");
            }


            //從資料庫 取得Member的資料 get("account")取的帳號資料
//            Optional<Member> user = memberRepository.findByAccount((String) account.get("account"));
            //設定該user的基本資料以及權限: credentials通常指密碼不傳也無妨
            Authentication authentication
                    = new UsernamePasswordAuthenticationToken(user.get().getAccount(), null, user.get().getAuthorities());
            log.warn("[JWTFilter] -> check role -> " + authentication.getAuthorities());
            //Filter中查詢使用者目的在將該請求代表的authentication資料放進去security的context
            //context是一種比較抽象的概念 可以把他想成該清求再security機制中的狀態
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private List<SimpleGrantedAuthority> convertToSimpleAuthorities(List<Role> authorities) {
        return authorities.stream()
                .map(auth -> new SimpleGrantedAuthority(auth.name()))
                .collect(Collectors.toList());
    }
}
