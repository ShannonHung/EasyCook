package com.seminar.easyCookWeb.entity.app_user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String account;
    private String password;

}
