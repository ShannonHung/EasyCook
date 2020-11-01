package com.seminar.easyCookWeb.Entity.User;

import com.seminar.easyCookWeb.Pojo.app_user.Department;
import com.seminar.easyCookWeb.Pojo.app_user.Role;
import lombok.Data;

@Data
public class EmployeeResponse {

    private Long id;
    private String account;
    private String email;
    private String username;
    private Department department;
    private String title;
    private String phone;
    private Role role;
}
