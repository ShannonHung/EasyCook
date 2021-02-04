package com.seminar.easyCookWeb.security;

import com.seminar.easyCookWeb.pojo.appUser.User;
import com.seminar.easyCookWeb.repository.users.EmployeeRepository;
import com.seminar.easyCookWeb.repository.users.MemberRepository;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

//這裡會是負責針對所有request進行filter的地方

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

//    private final JwtTokenProvider jwtTokenProvider;
    private JWTService jwtService;
    private Optional<User> user = null;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    public JwtAuthorizationFilter(AuthenticationManager authManager, JWTService jwtService, MemberRepository memberRepository, EmployeeRepository employeeRepository) {
        super(authManager);
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
        this.employeeRepository = employeeRepository;
    }

    //解析token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String token = jwtTokenProvider.resolveToken(req);
//        System.out.println("dofileter => " + token);
//        if (token != null) {
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
//            if (authentication != null) {
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//        chain.doFilter(req, res);
        //取得header裡面key=Authorization的value
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        //如果header裡面真的有東西
        if(authHeader != null){
            //只取得後面的部分
            String accessToken = authHeader.replace("Bearer ","");
            log.info("[AuthorizationFilter] => get Token without Bearer => " + accessToken);
            //解析token
            Map<String, Object> claims = jwtService.parseToken(accessToken);

            for ( String key : claims.keySet() ) {
                System.out.println( "[AuthorizationFilter] => check all key " + key );
            }

            log.info("[AuthorizationFilter] => try to get UserInfo from token =>" + claims.get("UserInfo"));

            log.info("[AuthorizationFilter] => try to get UserInfo from token tostring=>" + claims.get("UserInfo").toString());

            String username = (String) claims.get("UserInfo");
            log.info("[AuthorizationFilter] => memberRepository.findByAccount((String) username).isPresent() =>"  + memberRepository.findByAccount(username));
            log.info("[AuthorizationFilter] => try to get username from UserInfo=>" + username);

            //因為是HashMap account裡面有所有Member的資料
//            HashMap<String, Object> account = (HashMap<String, Object>) claims.get("UserInfo");


//            log.info("[AuthorizationFilter] => employeeRepository.findByAccount(username).isPresent() =>"  + employeeRepository.findByAccount(username).isPresent());

            if(memberRepository.findByAccount(username).isPresent()){
                log.info("[AuthorizationFilter] =>  user is member");
                //從資料庫 取得Member的資料 get("account")取的帳號資料
                user = Optional.of(memberRepository.findByAccount(username).get());
            }else if(employeeRepository.findByAccount(username).isPresent()){
                log.info("[AuthorizationFilter] =>  user is employee");
                user = Optional.of(employeeRepository.findByAccount(username).get());
            }else{
                log.error("JWTFilter Exception?!");
                throw new JwtException("JWTFilter Exception?!");
            }
            log.info("[AuthorizationFilter] => get actual user =>" + user.toString());

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
        chain.doFilter(request, response);
    }

}
