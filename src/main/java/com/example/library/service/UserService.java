package com.example.library.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.library.dao.LoginUser;
import com.example.library.dao.UserDao;
import com.example.library.mapper.UserMapper;
import com.example.library.utils.JwtUtil;
import com.example.library.utils.RedisCache;
import com.example.library.utils.ResponseResult;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private HttpServletResponse response;


    /**
     * 向数据库中添加一条用户信息
     * @param username 用户名 必需
     * @param password 密码 必需
     * @param email    邮件 必需
     * @param sex      性别 可选
     * @param phone    手机号 必需
     * @param role     用户身份 必需
     * @return
     */
    public ResponseResult insertUser(String username, String password, String email, String sex, String phone, String role) {
        UserDao user = new UserDao();
        boolean vaild = username.equals("")||password.equals("")||email.equals("") || phone.equals("")||role.equals("");
        if(!vaild){
            if(!(role.equals("student")||role.equals("admin"))){
                response.setStatus(507);
                return new ResponseResult<>(507, "注册失败，用户角色只能为student或admin", null);
            }
            user.setUsername(username);
            // 对密码进行加密
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setSex(sex);
            user.setPhone(phone);
            user.setRole(role);
            try{
                userMapper.insertUser(user);
                response.setStatus(200);
                return new ResponseResult<>(200, "注册成功", null);
            }catch(Exception e){
                response.setStatus(505);
                return new ResponseResult<>(505, "注册失败，该用户已经存在", null);
            }
        }else{
            response.setStatus(506);
            return new ResponseResult<>(506, "注册失败，注册数据缺失", null);
        }
    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    public ResponseResult login(UserDao user){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 认证失败
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("登录失败"); 
        }
        // 认证通过
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        // 保存信息至redis中
        redisCache.setCacheObject("Login:" + userid, loginUser);
        response.setStatus(200);
        return new ResponseResult<Map<String, String>>(200, "登录成功", map);
    }
}
