package com.contacts.service;

import com.contacts.dao.UserDao;
import com.contacts.dao.UserDaoImpl;
import com.contacts.entity.User;
import com.contacts.util.MD5Util;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (userDao.findByUsername(user.getUsername()) != null) {
            System.out.println("用户名已存在！");
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
        User user = userDao.findByUsername(username);
        if (user != null && user.getPassword().equals(MD5Util.encrypt(password))) {
            return user;
        }
        return null;
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && "admin".equals(user.getRole());
    }
}