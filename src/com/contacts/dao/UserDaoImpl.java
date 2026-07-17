package com.contacts.dao;

import com.contacts.entity.User;
import com.contacts.util.DBUtil;
import com.contacts.util.MD5Util;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 用户数据访问实现类
 *
 * 【类说明】
 * - 实现 UserDao 接口
 * - 负责所有用户相关的数据库操作
 * - 使用JDBC进行数据库访问
 *
 * 【数据库配置】
 * - 数据库：contact_system
 * - 用户表：t_user
 * - 连接池：通过DBUtil.getConnection()获取
 *
 * 【资源管理】
 * - 使用try-with-resources自动关闭资源
 * - Connection、PreparedStatement、ResultSet都会正确关闭
 * - 注册操作使用手动事务控制，确保数据一致性
 */
public class UserDaoImpl implements UserDao {

    /**
     * 注册新用户
     *
     * 【SQL语句】
     * INSERT INTO t_user (username, password, nickname, phone, email)
     * VALUES (?, ?, ?, ?, ?)
     *
     * 【参数设置】
     * 1. username - 用户名
     * 2. password - MD5加密后的密码
     * 3. nickname - 昵称
     * 4. phone - 电话
     * 5. email - 邮箱
     *
     * 【事务管理】
     * - 关闭自动提交（conn.setAutoCommit(false)）
     * - 执行插入操作
     * - 手动提交事务（conn.commit()）
     * - 发生异常时回滚（conn.rollback()）
     *
     * @param user 包含注册信息的User对象
     * @return true-注册成功，false-注册失败
     */
    @Override
    public boolean register(User user) {
        // SQL插入语句
        String sql = "INSERT INTO t_user (username, password, nickname, phone, email) VALUES (?, ?, ?, ?, ?)";

        // 定义数据库连接（需要手动关闭）
        Connection conn = null;

        try {
            // 【第一步】获取数据库连接
            conn = DBUtil.getConnection();

            // 【第二步】关闭自动提交，开启手动事务
            // 这样可以确保数据一致性，插入失败时可以回滚
            conn.setAutoCommit(false);

            // 【第三步】创建预编译语句对象
            PreparedStatement ps = conn.prepareStatement(sql);

            // 【第四步】设置参数（从1开始）
            ps.setString(1, user.getUsername());                    // 用户名
            ps.setString(2, MD5Util.encrypt(user.getPassword()));   // 密码（MD5加密）
            ps.setString(3, user.getNickname());                   // 昵称
            ps.setString(4, user.getPhone());                      // 电话
            ps.setString(5, user.getEmail());                     // 邮箱

            // 【第五步】执行插入
            int result = ps.executeUpdate();

            // 【第六步】提交事务
            conn.commit();

            // 【第七步】关闭资源
            ps.close();
            conn.close();

            // 返回插入是否成功（影响行数 > 0）
            return result > 0;

        } catch (SQLException e) {
            // 【异常处理】发生错误时回滚事务
            if (conn != null) {
                try {
                    // 回滚未提交的更改
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新用户信息
     *
     * 【SQL语句】
     * UPDATE t_user SET nickname=?, phone=?, email=? WHERE id=?
     *
     * 【更新字段】
     * - nickname: 昵称
     * - phone: 电话
     * - email: 邮箱
     * - 条件: id=?
     *
     * 【资源管理】
     * - 使用try-with-resources自动关闭连接和语句
     * - 无需手动关闭资源
     *
     * @param user 包含更新信息的User对象（需设置id）
     * @return true-更新成功，false-更新失败
     */
    @Override
    public boolean update(User user) {
        // SQL更新语句：更新昵称、电话、邮箱，条件是id
        String sql = "UPDATE t_user SET nickname=?, phone=?, email=? WHERE id=?";

        // 使用try-with-resources，自动关闭连接和语句
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 设置参数
            ps.setString(1, user.getNickname());  // 昵称
            ps.setString(2, user.getPhone());      // 电话
            ps.setString(3, user.getEmail());      // 邮箱
            ps.setInt(4, user.getId());            // 用户ID（条件）

            // 执行更新，返回影响行数
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据ID删除用户
     *
     * 【SQL语句】
     * DELETE FROM t_user WHERE id=?
     *
     * 【危险操作】此操作不可逆！
     *
     * @param userId 要删除的用户的ID
     * @return true-删除成功，false-删除失败
     */
    @Override
    public boolean deleteById(int userId) {
        String sql = "DELETE FROM t_user WHERE id=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 设置参数：用户ID
            ps.setInt(1, userId);

            // 执行删除
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据用户名查询用户
     *
     * 【SQL语句】
     * SELECT * FROM t_user WHERE username=?
     *
     * 【使用场景】
     * - 登录时验证用户名
     * - 注册时检查用户名是否已存在
     *
     * @param username 用户名
     * @return 找到返回User对象，未找到返回null
     */
    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM t_user WHERE username=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 设置参数：用户名
            ps.setString(1, username);

            // 执行查询，获取结果集
            try (ResultSet rs = ps.executeQuery()) {
                // 如果有结果（光标移动到下一行）
                if (rs.next()) {
                    // 提取用户信息并返回
                    return extractUserFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 未找到用户
        return null;
    }

    /**
     * 根据ID查询用户（方法1）
     *
     * 【SQL语句】
     * SELECT * FROM t_user WHERE id = ?
     *
     * @param id 用户ID
     * @return 找到返回User对象，未找到返回null
     */
    @Override
    public User selectById(int id) {
        String sql = "SELECT * FROM t_user WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置参数：用户ID
            pstmt.setInt(1, id);

            // 执行查询
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据ID查询用户（方法2）
     *
     * 【SQL语句】
     * SELECT * FROM t_user WHERE id = ?
     *
     * 【说明】与selectById功能相同，保留两个方法可能是为了兼容不同调用习惯
     *
     * @param id 用户ID
     * @return 找到返回User对象，未找到返回null
     */
    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM t_user WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从ResultSet中提取用户信息
     *
     * 【功能】
     * - 将ResultSet中的当前行数据提取到User对象中
     * - 封装了字段映射和类型转换的逻辑
     *
     * 【字段映射】
     * - id -> user.setId()
     * - username -> user.setUsername()
     * - password -> user.setPassword()
     * - nickname -> user.setNickname()
     * - phone -> user.setPhone()
     * - email -> user.setEmail()
     * - role -> user.setRole()
     * - reg_time -> user.setRegTime()（Timestamp转String）
     *
     * @param rs ResultSet对象（需在调用前将光标定位到有效行）
     * @return 填充好的User对象
     * @throws SQLException 如果访问字段出错
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        // 创建新的User对象
        User user = new User();

        // 设置用户ID（从数据库int型转换为Java int）
        user.setId(rs.getInt("id"));

        // 设置用户名
        user.setUsername(rs.getString("username"));

        // 设置密码（数据库中存储的是MD5加密后的字符串）
        user.setPassword(rs.getString("password"));

        // 设置昵称
        user.setNickname(rs.getString("nickname"));

        // 设置电话
        user.setPhone(rs.getString("phone"));

        // 设置邮箱
        user.setEmail(rs.getString("email"));

        // 设置角色
        user.setRole(rs.getString("role"));

        // 设置注册时间
        // 使用getTimestamp获取日期时间，转换为String存储
        Timestamp ts = rs.getTimestamp("reg_time");
        if (ts != null) {
            user.setRegTime(ts.toString());
        }

        return user;
    }
}
