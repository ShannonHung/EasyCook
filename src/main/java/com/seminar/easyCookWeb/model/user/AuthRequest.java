package com.seminar.easyCookWeb.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel("Token請求規格")
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @ApiModelProperty(value = "使用者帳號: Member or Employee", example = "employee001", required = true)
    private String account;
    @ApiModelProperty(value = "使用者密碼", example = "123", required = true)
    private String password;

}
