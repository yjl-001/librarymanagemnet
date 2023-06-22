package com.example.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.dao.UserDao;
import com.example.library.service.UserService;
import com.example.library.utils.ResponseResult;

import jakarta.servlet.http.HttpServletResponse;
/**
 * 用户模块的接口
 * 所有用户均可访问该模块下的接口
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletResponse response;

    /**
     * 用户注册接口
     * @param user 用户注册数据
     * @return {code, msg, data}
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseResult register(@RequestBody UserDao user){
        return userService.insertUser(user.getUsername(),user.getPassword(),user.getEmail(),user.getSex(),user.getPhone(),user.getRole());
    }

    /**
     * 用户登录
     * @param user 用户登录数据
     * @return {code, msg, data}
     */
    @RequestMapping(value = "/login",method=RequestMethod.GET)
    public ResponseResult login(@RequestBody UserDao user){
        try{
            return userService.login(user);
        }catch(RuntimeException e){
            response.setStatus(505);
            return new ResponseResult<>(505, "登录失败", null);
        }
    }
}
