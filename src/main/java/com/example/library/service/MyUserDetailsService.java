package com.example.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.library.dao.LoginUser;
import com.example.library.dao.UserDao;
import com.example.library.mapper.UserMapper;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    /**
     * 根据用户名在数据库中查询用户信息和用户的权限信息，并将其封装在UserDetails接口中供Spring Security的过滤器调用
     * 
     * @param username
     * @return UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 将来连接数据库根据账号查询用户信息和权限信息
        UserDao userDto = userMapper.getUser(username);
        // 当查询此用户不存在时，将抛出用户名未找到异常
        if (userDto == null) {
            throw new UsernameNotFoundException("No such user found, the user name is: " + username);
        }
        return new LoginUser(userDto);
    }

}
