package com.seminar.easyCookWeb.model.user;

import com.seminar.easyCookWeb.pojo.appUser.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("Member請求Entity樣式")
public class MemberRequest {
    @ApiModelProperty(value = "會員帳號", required = true, example = "member001")
    @NotBlank
    private String account;
    @ApiModelProperty(value = "會員密碼", required = true, example = "123")
    @NotBlank
    private String password;
    @ApiModelProperty(value = "會員姓名", required = true, example = "黃小美")
    private String username;
    @ApiModelProperty(value = "會員電話號碼", required = true, example = "0987654321")
    private String phone;
    @ApiModelProperty(value = "會員信箱", required = true, example = "email@gmail.com")
    private String email;
    @ApiModelProperty(value = "會員權限", required = true, example = "VIP, MEMBER")
    private Role role;

}
