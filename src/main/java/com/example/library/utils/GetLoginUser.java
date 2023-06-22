package com.example.library.utils;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.example.library.dao.LoginUser;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class GetLoginUser {
    @Autowired
    private RedisCache redisCache;

    /**
     * 从前端发送至后端的request中解析token获得当前用户信息
     * 
     * @param request
     * @return LoginUser 封装了用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            return null;
        }
        // 解析token
        String userId;
        try {
            Claims claim = JwtUtil.parseJWT(token);
            userId = claim.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("非法token");
        }
        // 从redis中获取用户信息
        String redisKey = "Login:" + userId;
        LoginUser loginUser = JSON.parseObject(JSON.toJSONString(redisCache.getCacheObject(redisKey)), LoginUser.class);
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("用户未登录");
        }
        return loginUser;
    }
}
