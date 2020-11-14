package com.seminar.easyCookWeb.Entity.User;

import com.seminar.easyCookWeb.Pojo.appUser.Department;
import com.seminar.easyCookWeb.Pojo.appUser.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Employee回應Entity樣式")
public class EmployeeResponse {
    @ApiModelProperty("員工ID")
    private Long id;
    @ApiModelProperty("員工帳號")
    private String account;
    @ApiModelProperty("員工信箱")
    private String email;
    @ApiModelProperty("員工姓名")
    private String username;
    @ApiModelProperty("員工部門")
    private Department department;
    @ApiModelProperty("員工職稱")
    private String title;
    @ApiModelProperty("員工電話號碼")
    private String phone;
    @ApiModelProperty("權限")
    private Role role;
}
