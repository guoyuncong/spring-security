package com.yc.security.base.constants;

/**
 * JWT 常量类
 */
public class JwtConstants {

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 有效载荷 - 用户信息
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 自动刷新缓存限制时间
     * 过期时间 - 当前时间 <= MILLIS_MINUTE_TEN，刷新 Token
     */
    public static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    /**
     * 默认登录延长时间
     */
    public static final long MILLIS_MINUTE = 60 * 1000L;
}
