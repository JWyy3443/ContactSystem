package com.contacts.service;

import com.contacts.entity.User;

public interface UserService {
    boolean register(User user);
    boolean updateUserInfo(User user);
    boolean deleteUser(int userId);
    User login(String username, String password);
    boolean isAdmin(User user);
    User findById(int userId);

}