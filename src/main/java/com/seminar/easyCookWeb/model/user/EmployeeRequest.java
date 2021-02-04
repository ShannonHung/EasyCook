package com.seminar.easyCookWeb.model.user;

import com.seminar.easyCookWeb.pojo.appUser.Department;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("Employee請求Entity樣式")
public class EmployeeRequest {
    @ApiModelProperty(value = "員工帳號", example = "employee001", required = true)
    private String account;
    @ApiModelProperty(value = "員工密碼", example = "123", required = true)
    private String password;
    @ApiModelProperty(value = "員工姓名", example = "林小明", required = true)
    private String username;
    @ApiModelProperty(value = "員工所屬部門", example = "Sales", required = true)
    private Department department;
    @ApiModelProperty(value = "員工職稱", example = "經理", required = true)
    private String title;
    @ApiModelProperty(value = "員工電話號碼", example = "0912345678", required = true)
    private String phone;
    @ApiModelProperty(value = "員工信箱", example = "email@gamil.com", required = true)
    private String email;
//    @NotBlank //NotEmpty不能為null or "" , 可以為 " "
//    private Role role;

}
