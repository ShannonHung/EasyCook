package com.seminar.easyCookWeb.entity.app_user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seminar.easyCookWeb.entity.app_user.UserAuthority;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@ToString
public class MemberRequest {
    @NotBlank //裡面一定要有內容 不能null or "" or " "
    private String account;
    @NotBlank
    private String password;
    @NotBlank
    private String username;
    @NotBlank
    private String phoneNum;
    @NotBlank
    private String email;
    @NotEmpty //不能為null or "" , 可以為 " "
    private List<UserAuthority> authorities;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<UserAuthority> authorities) {
        this.authorities = authorities;
    }
}
