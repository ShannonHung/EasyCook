package com.seminar.easyCookWeb.Entity.User;

import com.seminar.easyCookWeb.Pojo.app_user.Department;
import com.seminar.easyCookWeb.Pojo.app_user.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("Employee請求Entity樣式")
public class EmployeeRequest {
    @ApiModelProperty(value = "員工帳號", example = "employee001", required = true)
    @NotBlank //裡面一定要有內容 不能null or "" or " "
    private String account;
    @ApiModelProperty(value = "員工密碼", example = "123", required = true)
    @NotBlank
    private String password;
    @ApiModelProperty(value = "員工姓名", example = "林小明", required = true)
    @NotBlank
    private String username;
    @NotBlank
    @ApiModelProperty(value = "員工所屬部門", example = "Sales", required = true)
    private Department department;
    @NotBlank
    @ApiModelProperty(value = "員工職稱", example = "經理", required = true)
    private String title;
    @NotBlank
    @ApiModelProperty(value = "員工電話號碼", example = "0912345678", required = true)
    private String phone;
    @NotBlank
    @ApiModelProperty(value = "員工信箱", example = "email@gamil.com", required = true)
    private String email;
//    @NotBlank //NotEmpty不能為null or "" , 可以為 " "
//    private Role role;

}
