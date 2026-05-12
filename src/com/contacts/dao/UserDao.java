package com.contacts.dao;

import com.contacts.entity.User;
import java.util.List;

public interface UserDao {
    boolean register(User user);                 // 注册
    boolean update(User user);                   // 修改信息
    boolean deleteById(int userId);               // 注销（根据ID删除）
    User findByUsername(String username);        // 根据用户名查找
    User findById(int id);
}