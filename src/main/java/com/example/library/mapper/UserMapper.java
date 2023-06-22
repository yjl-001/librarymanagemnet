package com.example.library.mapper;

import java.sql.SQLIntegrityConstraintViolationException;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import com.example.library.dao.UserDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    
    /**
     * 添加一个用户
     * @param user 用户数据
     */
    @Insert("INSERT INTO user(username, password, email, sex, phone, role) VALUES (#{user.username}, #{user.password}, #{user.email}, #{user.sex},#{user.phone},#{user.role})")
    void insertUser(@Param(value = "user") UserDao user);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return UserDao
     */
    @Select("SELECT * FROM user WHERE username = #{username}")
    UserDao getUser(@Param(value = "username") String username);
}
