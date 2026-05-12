package com.contacts.dao;

import com.contacts.entity.User;
import java.util.List;
import java.util.Map;

public interface AdminDao {
    List<User> findAllUsers();                // 查看所有用户
    List<User> searchUsers(String keyword);   // 根据用户名或昵称模糊查询
    Map<String, Integer> getUserCountByRole(); // 按角色统计
    int getTotalUserCount();                   // 总用户数
}