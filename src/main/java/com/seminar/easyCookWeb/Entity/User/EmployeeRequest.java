package com.seminar.easyCookWeb.Entity.User;

import com.seminar.easyCookWeb.Pojo.app_user.Department;
import com.seminar.easyCookWeb.Pojo.app_user.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmployeeRequest {
    @NotBlank //裡面一定要有內容 不能null or "" or " "
    private String account;
    @NotBlank
    private String password;
    @NotBlank
    private String username;
    @NotBlank
    private Department department;
    @NotBlank
    private String title;
    @NotBlank
    private String phone;
    @NotBlank
    private String email;
//    @NotBlank //NotEmpty不能為null or "" , 可以為 " "
//    private Role role;

}
