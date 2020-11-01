package com.seminar.easyCookWeb.Config;

import com.seminar.easyCookWeb.Pojo.app_user.Role;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@EnableWebSecurity //該標記已經冠上@Configuration標記
@EnableGlobalMethodSecurity(prePostEnabled=true) //prePostEnabled = true 会解锁 @PreAuthorize 和 @PostAuthorize 两个注解
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    private UserDetailService userDetailsService;


    @Autowired
    public SecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter, UserDetailService userDetailsService){
        super();
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Override //於此設置API的授權規則，若未設計API會恢復到能存取的狀態
    protected void configure(HttpSecurity http) throws Exception {
//         "/member" 這個API底下的所有GET請求需要透過身分驗證才可以存取
        http.authorizeRequests() // 使用「authorizeRequests」方法開始自訂授權規則
                .antMatchers("/h2/**").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/**", "/member/register", "/employee/register").permitAll() //供前端取得token
//                .anyRequest().hasAnyRole("EMPLOYEE")
                .anyRequest().authenticated()
                .and() //加入jwtFilter自己做的, UsernamePasswordAuthenticationFilter是用來處理表單form形式的登入請求
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
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
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    //會在JWTService.class被Autowire
    @Override
    @Bean //藉由Bean標記，才可以在spring boot初始化就建立才能Autowired
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}