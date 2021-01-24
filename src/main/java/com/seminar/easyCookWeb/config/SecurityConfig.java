package com.seminar.easyCookWeb.config;

import com.seminar.easyCookWeb.exception.handler.ExceptionHandlerFilter;
import com.seminar.easyCookWeb.exception.handler.RestAccessDeniedHandler;
import com.seminar.easyCookWeb.exception.handler.RestAuthenticationEntryPoint;
import com.seminar.easyCookWeb.repository.users.EmployeeRepository;
import com.seminar.easyCookWeb.repository.users.MemberRepository;
import com.seminar.easyCookWeb.security.JwtAuthenticationFilter;
import com.seminar.easyCookWeb.security.JwtAuthorizationFilter;
import com.seminar.easyCookWeb.security.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity //該標記已經冠上@Configuration標記
@EnableGlobalMethodSecurity(prePostEnabled=true) //prePostEnabled = true 会解锁 @PreAuthorize 和 @PostAuthorize 两个注解
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private UserDetailService userDetailService;
    private RestAuthenticationEntryPoint authenticationEntryPoint; //帳號密碼不正確 或是尚未登入
    private RestAccessDeniedHandler accessDeniedHandler; //沒有權限存取

    @Autowired
    public SecurityConfig(UserDetailService userDetailService, RestAuthenticationEntryPoint authenticationEntryPoint, RestAccessDeniedHandler accessDeniedHandler) {
        super();
        this.userDetailService = userDetailService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }


    @Override //於此設置API的授權規則，若未設計API會恢復到能存取的狀態
    protected void configure(HttpSecurity http) throws Exception {
//         "/member" 這個API底下的所有GET請求需要透過身分驗證才可以存取
        http.authorizeRequests() // 使用「authorizeRequests」方法開始自訂授權規則
                .and() //加入jwtFilter自己做的, UsernamePasswordAuthenticationFilter是用來處理表單form形式的登入請求
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthorizationFilter.class)
                .addFilter(new JwtAuthenticationFilter(jwtConfig, authenticationManager(), jwtService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtService, memberRepository, employeeRepository))
                .authorizeRequests()
                .antMatchers(jwtConfig.getUrl()).anonymous()
                .antMatchers("/h2/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/**", "/api/member/register", "/api/employee/register").permitAll() //供前端取得token
                .anyRequest().authenticated()
                .and()
                .cors().and()
                .csrf().disable()
                .logout().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //因為Spring Security預設會禁止iframe的東西，所以我們把它disable，查詢h2-console才能看到frame的畫面
        http.headers().frameOptions().disable();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    /**
     * 此處覆寫另一個「configure」方法，對「AuthenticationManagerBuilder」物件傳入UserDetailsService。接著傳入密碼加密器，這邊選用「BCryptPasswordEncoder」
     * 如此一來，登入時輸入的密碼便會被加密，與資料庫中使用者的已加密密碼進行比對。
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("admin").password(new BCryptPasswordEncoder().encode("123")).roles(Role.ROLE_ADMIN);
        auth.userDetailsService(userDetailService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }


    //會在JWTService.class被Autowire
    @Override
    @Bean //藉由Bean標記，才可以在spring boot初始化就建立才能Autowired
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}