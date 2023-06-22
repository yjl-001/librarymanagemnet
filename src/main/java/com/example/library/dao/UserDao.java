package com.example.library.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDao {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String sex;
    private String phone;
    private String role;
}
