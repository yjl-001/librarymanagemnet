package com.example.library.filter;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.alibaba.fastjson.JSON;
import com.example.library.Exception.IdentifyException;
import com.example.library.dao.LoginUser;
import com.example.library.utils.JwtUtil;
import com.example.library.utils.RedisCache;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token
        String userId;
        try {
            Claims claim = JwtUtil.parseJWT(token);
            userId = claim.getSubject();
            // 从redis中获取用户信息
            String redisKey = "Login:" + userId;
            LoginUser loginUser = JSON.parseObject(JSON.toJSONString(redisCache.getCacheObject(redisKey)),
                    LoginUser.class);
            if (Objects.isNull(loginUser)) {
                // 将异常发送给GlobalExceptionHandler进行处理
                resolver.resolveException(request,response,     null,new IdentifyException("用户未登录"));
            }else{
                // 存入SecurityContext
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        loginUser,
                        null, loginUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            // 将异常发送给GlobalExceptionHandler进行处理
            resolver.resolveException(request, response, null, new IdentifyException("非法token"));
        }
    }


}
