package com.contacts.service;

import com.contacts.dao.AdminDao;
import com.contacts.dao.AdminDaoImpl;
import com.contacts.entity.User;

import java.util.List;
import java.util.Map;

public class AdminServiceImpl implements AdminService {
    private AdminDao adminDao = new AdminDaoImpl();

    @Override
    public List<User> getAllUsers() {
        return adminDao.findAllUsers();
    }

    @Override
    public List<User> searchUsers(String keyword) {
        return adminDao.searchUsers(keyword);
    }

    @Override
    public void printStatistics() {
        System.out.println("\n========== 系统统计信息 ==========");
        int total = adminDao.getTotalUserCount();
        System.out.println("总注册用户数: " + total);
        Map<String, Integer> roleMap = adminDao.getUserCountByRole();
        for (Map.Entry<String, Integer> entry : roleMap.entrySet()) {
            System.out.println("角色 [" + entry.getKey() + "] : " + entry.getValue() + " 人");
        }
        System.out.println("======================11============\n");
    }
}