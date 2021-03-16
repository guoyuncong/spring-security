package com.yc.security.base.constants;

/**
 * Redis 常量类
 */
public class RedisConstants {

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_USER_KEY = "login_tokens:";

    /**
     * 登录验证码 redis key
     */
    public static final String LOGIN_CAPTCHA_KEY = "login_captcha:";
}
