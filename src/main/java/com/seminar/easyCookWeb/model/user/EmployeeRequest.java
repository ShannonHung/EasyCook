package com.seminar.easyCookWeb.model.user;

import com.seminar.easyCookWeb.pojo.appUser.Department;
import com.seminar.easyCookWeb.pojo.appUser.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ApiModel("Employee請求Entity樣式")
public class EmployeeRequest {
    @ApiModelProperty(value = "員工帳號", example = "employee001", required = true)
    @NotBlank
    private String account;
    @NotBlank
    @ApiModelProperty(value = "員工密碼", example = "123", required = true)
    private String password;
    @NotBlank
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
    @NotNull //NotEmpty不能為null or "" , 可以為 " "
    @ApiModelProperty(value = "權限", example = "ADMIN, ROOT, EMPLOYEE", required = true)
    private Role role;

}
