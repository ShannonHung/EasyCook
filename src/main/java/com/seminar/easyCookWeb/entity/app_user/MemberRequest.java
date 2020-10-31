package com.seminar.easyCookWeb.entity.app_user;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
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
    private Role role;

}
