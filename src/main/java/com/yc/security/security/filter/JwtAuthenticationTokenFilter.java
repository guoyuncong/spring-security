package com.yc.security.security.filter;

import com.yc.security.security.LoginUser;
import com.yc.security.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token 过滤器，验证 Token 有效性
 */
@Configuration
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 获得当前 LoginUser
        LoginUser loginUser = tokenService.getLoginUser();
        // 如果存在 LoginUser ，并且未认证过
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (loginUser != null && authentication == null) {
            // 校验 Token 有效性
            tokenService.verifyToken(loginUser);
            // 创建 UsernamePasswordAuthenticationToken 对象，设置到 SecurityContextHolder 中
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        // 继续过滤器
        chain.doFilter(request, response);
    }
}
