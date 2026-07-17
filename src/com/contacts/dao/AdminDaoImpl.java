package com.contacts.dao;

import com.contacts.entity.User;
import com.contacts.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员数据访问实现类
 *
 * 【类说明】
 * - 实现 AdminDao 接口
 * - 负责管理员相关的数据查询操作
 * - 使用JDBC进行数据库访问
 *
 * 【数据库配置】
 * - 数据库：contact_system
 * - 用户表：t_user
 *
 * 【资源管理】
 * - 使用try-with-resources自动关闭资源
 * - 所有SQL操作都正确关闭Statement和ResultSet
 */
public class AdminDaoImpl implements AdminDao {

    /**
     * 查询所有用户
     *
     * 【SQL语句】
     * SELECT * FROM t_user ORDER BY id
     *
     * 【功能】
     * - 获取t_user表中的所有记录
     * - 按ID升序排列
     *
     * @return 包含所有User对象的List集合
     */
    @Override
    public List<User> findAllUsers() {
        // 创建用于存储用户对象的列表
        List<User> users = new ArrayList<>();

        // SQL查询语句：查询所有用户，按ID排序
        String sql = "SELECT * FROM t_user ORDER BY id";

        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // 遍历结果集
            while (rs.next()) {
                // 提取当前行的用户信息并添加到列表
                users.add(extractUser(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 返回用户列表（可能为空List）
        return users;
    }

    /**
     * 搜索用户（模糊查询）
     *
     * 【SQL语句】
     * SELECT * FROM t_user WHERE username LIKE ? OR nickname LIKE ?
     *
     * 【功能】
     * - 根据关键词搜索用户
     * - 同时匹配用户名和昵称
     * - 使用SQL LIKE实现模糊搜索
     *
     * 【参数绑定】
     * - ?1 = '%keyword%' （用于username匹配）
     * - ?2 = '%keyword%' （用于nickname匹配）
     *
     * @param keyword 搜索关键词
     * @return 匹配的用户List集合
     */
    @Override
    public List<User> searchUsers(String keyword) {
        List<User> users = new ArrayList<>();

        // SQL语句：使用LIKE进行模糊匹配
        String sql = "SELECT * FROM t_user WHERE username LIKE ? OR nickname LIKE ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 构建模糊匹配字符串：前后加上%
            // 例如：keyword="张" -> "%张%"
            // 这样可以匹配包含"张"的任何字符串
            String like = "%" + keyword + "%";

            // 设置第一个参数：username的匹配条件
            ps.setString(1, like);

            // 设置第二个参数：nickname的匹配条件
            ps.setString(2, like);

            // 执行查询
            try (ResultSet rs = ps.executeQuery()) {
                // 遍历结果集
                while (rs.next()) {
                    users.add(extractUser(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * 按角色统计用户数量
     *
     * 【SQL语句】
     * SELECT role, COUNT(*) AS cnt FROM t_user GROUP BY role
     *
     * 【功能】
     * - 按role字段分组
     * - 统计每组的用户数量
     * - 返回Map：key=角色名，value=用户数量
     *
     * @return 包含角色和数量的Map对象
     */
    @Override
    public Map<String, Integer> getUserCountByRole() {
        // 创建HashMap存储统计结果
        Map<String, Integer> map = new HashMap<>();

        // SQL语句：按角色分组统计
        String sql = "SELECT role, COUNT(*) AS cnt FROM t_user GROUP BY role";

        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // 遍历分组结果
            while (rs.next()) {
                // 获取角色名称
                String role = rs.getString("role");
                // 获取该角色的用户数量
                int count = rs.getInt("cnt");
                // 存入Map
                map.put(role, count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * 获取用户总数
     *
     * 【SQL语句】
     * SELECT COUNT(*) FROM t_user
     *
     * 【功能】
     * - 统计t_user表中的记录总数
     * - 使用COUNT(*)函数
     *
     * @return 用户总数
     */
    @Override
    public int getTotalUserCount() {
        String sql = "SELECT COUNT(*) FROM t_user";

        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // COUNT(*)返回单行单列的结果
            // 使用rs.next()移动光标到第一行
            if (rs.next()) {
                // 获取第一列的值（索引从1开始）
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 发生错误时返回0
        return 0;
    }

    /**
     * 从ResultSet中提取用户信息
     *
     * 【功能】
     * - 将ResultSet的当前行数据提取到User对象
     * - 封装字段映射逻辑
     *
     * 【字段映射】
     * - id, username, password, nickname, phone, email, role, reg_time
     *
     * @param rs ResultSet对象（光标应在有效行）
     * @return User对象
     * @throws SQLException 如果访问字段出错
     */
    private User extractUser(ResultSet rs) throws SQLException {
        User u = new User();

        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setNickname(rs.getString("nickname"));
        u.setPhone(rs.getString("phone"));
        u.setEmail(rs.getString("email"));
        u.setRole(rs.getString("role"));

        Timestamp ts = rs.getTimestamp("reg_time");
        if (ts != null) {
            u.setRegTime(ts.toString());
        }

        return u;
    }
}
