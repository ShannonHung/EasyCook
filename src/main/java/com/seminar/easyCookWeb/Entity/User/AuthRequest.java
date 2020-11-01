package com.seminar.easyCookWeb.Entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String account;
    private String password;

}
