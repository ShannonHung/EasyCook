package com.seminar.easyCookWeb.entity.app_user;

import lombok.Data;

import java.util.List;

@Data
public class MemberResponse {

    private Long id;
    private String account;
    private String email;
    private String username;
    private String phoneNum;
    private Role role;

}
