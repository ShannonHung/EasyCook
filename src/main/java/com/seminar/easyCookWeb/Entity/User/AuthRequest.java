package com.seminar.easyCookWeb.Entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Token請求規格")
public class AuthRequest {
    @ApiModelProperty(value = "使用者帳號: Member or Employee", example = "employee001", required = true)
    private String account;
    @ApiModelProperty(value = "使用者密碼", example = "123", required = true)
    private String password;

}
