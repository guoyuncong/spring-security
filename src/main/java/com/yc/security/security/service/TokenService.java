package com.yc.security.security.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.yc.security.base.constants.JwtConstants;
import com.yc.security.base.constants.RedisConstants;
import com.yc.security.base.utils.RedisUtil;
import com.yc.security.base.utils.ServletUtils;
import com.yc.security.security.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 验证处理
 * Json Web Token(JWT) || Redis User Token
 */
@Component
public class TokenService {

    /**
     * 令牌秘钥
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * 令牌有效期
     */
    @Value("${jwt.expireTime}")
    private int expireTime;

    /**
     * 创建 JWT 令牌
     *
     * @param loginUser 登录信息
     * @return 令牌
     */
    public String createJwtToken(LoginUser loginUser) {
        // 设置 LoginUser 的用户唯一标识
//        String token = UUID.fastUUID().toString().replace("-", "");
        String token = "";
        loginUser.setToken(token);
        // 将用户信息记入缓存
        refreshToken(loginUser);
        // 生成 JWT 的 Token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConstants.LOGIN_USER_KEY, token);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser() {
        // 获取请求携带的令牌
        String token = getToken(ServletUtils.getRequest());
        if (StrUtil.isNotEmpty(token)) {
            // 解析 JWT 的 Token
            Claims claims = parseToken(token);
            // 获取 Token 中的用户信息
            String uuid = (String) claims.get(JwtConstants.LOGIN_USER_KEY);
            // 从 Redis 缓存中，获得对应的 UserDetail
            String userJson = RedisUtil.get(RedisConstants.LOGIN_USER_KEY + uuid);
            if (StrUtil.isNotEmpty(userJson)) {
                return JSONObject.parseObject(userJson, LoginUser.class);
            }
            return null;
        }
        return null;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取 JWT token
     *
     * @param request 请求
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isNotEmpty(token) && token.startsWith(JwtConstants.TOKEN_PREFIX)) {
            token = token.replace(JwtConstants.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * Redis 中如果用户信息即将过期，延长时间
     *
     * @param loginUser 登录用户
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        // 相差不足 20 分钟，自动刷新缓存
        if (expireTime - currentTime <= JwtConstants.MILLIS_MINUTE_TEN) {
            String token = loginUser.getToken();
            loginUser.setToken(token);
            refreshToken(loginUser);
        }
    }

    /**
     * Redis 延长用户信息有效期
     *
     * @param loginUser 登录信息
     */
    private void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * JwtConstants.MILLIS_MINUTE);
        // 根据 uuid 将 loginUser 缓存
        String userKey = RedisConstants.LOGIN_USER_KEY + loginUser.getToken();
        RedisUtil.set(userKey, JSONObject.toJSONString(loginUser), expireTime, TimeUnit.DAYS);
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StrUtil.isNotEmpty(token)) {
            // 根据 uuid 将 loginUser 缓存
            String userKey = RedisConstants.LOGIN_USER_KEY + token;
            // 删除缓存
            RedisUtil.delete(userKey);
        }
    }

}
