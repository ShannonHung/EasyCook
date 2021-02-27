package com.seminar.easyCookWeb.model.user;

import com.seminar.easyCookWeb.pojo.appUser.Department;
import com.seminar.easyCookWeb.pojo.appUser.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("Employee更新請求Entity樣式")
public class UpdatePwd {
    @NotBlank
    @ApiModelProperty(value = "員工新密碼", example = "123", required = true)
    private String newpassword;
    @NotBlank
    @ApiModelProperty(value = "員工舊密碼", example = "123", required = true)
    private String prepassword;
}
