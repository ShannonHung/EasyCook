package com.seminar.easyCookWeb.model.user;

import com.seminar.easyCookWeb.pojo.appUser.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Member回應Entity樣式")
public class MemberResponse {
    @ApiModelProperty("會員ID")
    private Long id;
    @ApiModelProperty("會員帳號")
    private String account;
    @ApiModelProperty("會員信箱")
    private String email;
    @ApiModelProperty("會員姓名")
    private String username;
    @ApiModelProperty("會員電話號碼")
    private String phone;
    @ApiModelProperty("權限")
    private Role role;

}
