package com.contacts.service;

import com.contacts.entity.User;
import java.util.List;
import java.util.Map;

public interface AdminService {
    List<User> getAllUsers();
    List<User> searchUsers(String keyword);
    void printStatistics();          // 打印统计信息
}