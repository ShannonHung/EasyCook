package com.seminar.easyCookWeb.entity.app_user;

import lombok.Getter;

@Getter
public enum Role {
    ROOT, ADMIN, MEMBER;
    public static final String ROLE_ROOT = "ROLE_ROOT";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_MEMBER = "ROLE_MEMBER";

    static {
        ROOT.name = ROLE_ROOT;
        ADMIN.name = ROLE_ADMIN;
        MEMBER.name = ROLE_MEMBER;
    }

    private String name;

    Role() {
    }

    public String getName() {
        return name;
    }
}
