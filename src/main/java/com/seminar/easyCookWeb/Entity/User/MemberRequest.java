package com.seminar.easyCookWeb.Entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("Member請求Entity樣式")
public class MemberRequest {
    @NotBlank //裡面一定要有內容 不能null or "" or " "
    @ApiModelProperty(value = "會員帳號", required = true, example = "member001")
    private String account;
    @NotBlank
    @ApiModelProperty(value = "會員密碼", required = true, example = "123")
    private String password;
    @NotBlank
    @ApiModelProperty(value = "會員姓名", required = true, example = "黃小美")
    private String username;
    @NotBlank
    @ApiModelProperty(value = "會員電話號碼", required = true, example = "0987654321")
    private String phone;
    @NotBlank
    @ApiModelProperty(value = "會員信箱", required = true, example = "email@gmail.com")
    private String email;
//    @NotEmpty //不能為null or "" , 可以為 " "
//    private Role role;

}
