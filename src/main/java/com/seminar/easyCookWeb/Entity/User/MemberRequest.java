package com.seminar.easyCookWeb.Entity.User;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MemberRequest {
    @NotBlank //裡面一定要有內容 不能null or "" or " "
    private String account;
    @NotBlank
    private String password;
    @NotBlank
    private String username;
    @NotBlank
    private String phone;
    @NotBlank
    private String email;
//    @NotEmpty //不能為null or "" , 可以為 " "
//    private Role role;

}
