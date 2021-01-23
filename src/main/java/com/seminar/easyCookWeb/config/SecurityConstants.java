package com.seminar.easyCookWeb.config;

//放Token的基本設定
public class SecurityConstants {
    public static final String SECRET = "oursecretkey";
    public static final int EXPIRATION_TIME_MINUTES = 1; //10day 864000=10*24*60*60s
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "accessToken";
}
