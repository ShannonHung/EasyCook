package com.seminar.easyCookWeb.pojo.appUser;

import lombok.Getter;

@Getter
public enum Role {
    ROOT, ADMIN, MEMBER, EMPLOYEE;
    public static final String ROLE_ROOT = "ROLE_ROOT";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_MEMBER = "ROLE_MEMBER";
    public static final String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";

    private String name;
    static {
        ROOT.name = ROLE_ROOT;
        ADMIN.name = ROLE_ADMIN;
        MEMBER.name = ROLE_MEMBER;
        EMPLOYEE.name = ROLE_EMPLOYEE;
    }


    Role() {
    }

    public String getName() {
        return name;
    }
}
