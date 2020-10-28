package com.seminar.easyCookWeb.Config;

import com.seminar.easyCookWeb.Service.AuthenticationService;
import com.seminar.easyCookWeb.entity.app_user.UserAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity //該標記已經冠上@Configuration標記
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationService authenticationService;
    public SecurityConfig(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @Override //於此設置API的授權規則，若未設計API會恢復到能存取的狀態
    protected void configure(HttpSecurity http) throws Exception {
//         "/member" 這個API底下的所有GET請求需要透過身分驗證才可以存取
        http.authorizeRequests() // 使用「authorizeRequests」方法開始自訂授權規則
                .antMatchers(HttpMethod.GET, "/member/allMembers").hasAuthority(UserAuthority.ADMIN.name()) //其他GET請求的API都可以存取
                .antMatchers("/h2/**").hasAuthority(UserAuthority.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/member/**").permitAll() //但是POST member可以請求
                .antMatchers(HttpMethod.GET, "/member/{id}").hasAuthority(UserAuthority.MEMBER.name())
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin();
        //因為Spring Security預設會禁止iframe的東西，所以我們把它disable，查詢h2-console才能看到frame的畫面
        http.headers().frameOptions().disable();
    }

    /**
     * 此處覆寫另一個「configure」方法，對「AuthenticationManagerBuilder」物件傳入UserDetailsService。接著傳入密碼加密器，這邊選用「BCryptPasswordEncoder」
     * 如此一來，登入時輸入的密碼便會被加密，與資料庫中使用者的已加密密碼進行比對。
     * @param auth
     * @throws Exception
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService)
//                .passwordEncoder(new BCryptPasswordEncoder());
//    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.authenticationService);
    }
}