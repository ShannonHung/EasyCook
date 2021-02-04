package com.seminar.easyCookWeb.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Member請求Entity樣式")
public class MemberRequest {
    @ApiModelProperty(value = "會員帳號", required = true, example = "member001")
    private String account;
    @ApiModelProperty(value = "會員密碼", required = true, example = "123")
    private String password;
    @ApiModelProperty(value = "會員姓名", required = true, example = "黃小美")
    private String username;
    @ApiModelProperty(value = "會員電話號碼", required = true, example = "0987654321")
    private String phone;
    @ApiModelProperty(value = "會員信箱", required = true, example = "email@gmail.com")
    private String email;
//    @NotEmpty //不能為null or "" , 可以為 " "
//    private Role role;

}
