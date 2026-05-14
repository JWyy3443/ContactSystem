package com.contacts.service;

import com.contacts.dao.UserDao;
import com.contacts.dao.UserDaoImpl;
import com.contacts.entity.User;
import com.contacts.util.MD5Util;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public boolean register(User user) {
        // ����û����Ƿ��Ѵ���
        if (userDao.findByUsername(user.getUsername()) != null) {
            System.out.println("�û����Ѵ��ڣ�");
            return false;
        }
        return userDao.register(user);
    }

    @Override
    public boolean updateUserInfo(User user) {
        return userDao.update(user);
    }

    @Override
    public boolean deleteUser(int userId) {
        return userDao.deleteById(userId);
    }

    @Override
    public User login(String username, String password) {
        long startTime = System.currentTimeMillis();
        System.out.println("=== Login attempt [" + startTime + "] ===");
        System.out.println("Username: " + username);
        
        try {
            User user = userDao.findByUsername(username);
            System.out.println("Query completed in " + (System.currentTimeMillis() - startTime) + "ms");
            
            if (user != null) {
                System.out.println("User found: " + user.getUsername() + ", ID: " + user.getId());
                String inputHash = MD5Util.encrypt(password);
                boolean passwordMatch = user.getPassword().equals(inputHash);
                System.out.println("Password match: " + passwordMatch);
                
                if (passwordMatch) {
                    System.out.println("Login SUCCESS");
                    return user;
                } else {
                    System.out.println("Login FAILED - password mismatch");
                }
            } else {
                System.out.println("Login FAILED - user not found");
            }
        } catch (Exception e) {
            System.out.println("Login FAILED - exception: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== Login attempt [" + startTime + "] ended ===");
        return null;
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && "admin".equals(user.getRole());
    }
    @Override
    public User findById(int userId) {
        return userDao.selectById(userId);
    }
}