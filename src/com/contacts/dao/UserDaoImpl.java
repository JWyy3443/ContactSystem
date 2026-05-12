package com.contacts.dao;

import com.contacts.entity.User;
import com.contacts.util.DBUtil;
import com.contacts.util.MD5Util;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserDaoImpl implements UserDao {

    @Override
    public boolean register(User user) {
        String sql = "INSERT INTO t_user (username, password, nickname, phone, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, MD5Util.encrypt(user.getPassword())); // √‹¬Îº”√‹
            ps.setString(3, user.getNickname());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE t_user SET nickname=?, phone=?, email=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getNickname());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteById(int userId) {
        String sql = "DELETE FROM t_user WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM t_user WHERE username=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM t_user WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password")); // √‹Œƒ
        user.setNickname(rs.getString("nickname"));
        user.setPhone(rs.getString("phone"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        Timestamp ts = rs.getTimestamp("reg_time");
        if (ts != null) {
            user.setRegTime(ts.toLocalDateTime());
        }
        return user;
    }
}