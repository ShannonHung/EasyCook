package com.seminar.easyCookWeb.entity.app_user;

import java.util.List;

public class MemberResponse {
    private Long id;
    private String account;
    private String email;
    private String username;
    private String phoneNum;
    private List<UserAuthority> authorityList;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UserAuthority> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<UserAuthority> authorityList) {
        this.authorityList = authorityList;
    }
}
