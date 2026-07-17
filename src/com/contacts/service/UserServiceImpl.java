package com.contacts.service;

import com.contacts.dao.UserDao;
import com.contacts.dao.UserDaoImpl;
import com.contacts.entity.User;
import com.contacts.util.MD5Util;

/**
 * 用户服务实现类
 *
 * 【类说明】
 * - 实现 UserService 接口定义的业务逻辑
 * - 负责处理用户相关的业务流程和数据校验
 * - 协调 DAO 层完成数据库操作
 *
 * 【依赖关系】
 * - 依赖 UserDao 接口进行数据库操作
 * - 依赖 MD5Util 工具进行密码加密
 *
 * 【设计模式】
 * - 采用面向接口编程，依赖注入 DAO 实现
 * - 业务逻辑与数据访问分离
 */
public class UserServiceImpl implements UserService {

    /** 用户数据访问层接口，通过依赖注入获取实现 */
    private UserDao userDao = new UserDaoImpl();

    /**
     * 注册新用户
     *
     * 【业务逻辑】
     * 1. 检查用户名是否已存在（防止重复注册）
     * 2. 如果用户名存在，返回false
     * 3. 如果用户名不存在，调用DAO执行注册
     *
     * 【安全性】
     * - 密码在 DAO 层被 MD5 加密后存储
     * - 用户名唯一性约束由数据库保证
     *
     * @param user 包含注册信息的User对象
     * @return true-注册成功，false-注册失败（用户名已存在）
     */
    @Override
    public boolean register(User user) {
        // 【第一步】检查用户名是否已存在
        // 调用DAO根据用户名查询用户
        if (userDao.findByUsername(user.getUsername()) != null) {
            // 用户名已存在，打印日志并返回失败
            System.out.println("用户已存在，注册失败");
            return false;
        }

        // 【第二步】执行注册
        // 用户名不存在，调用DAO的register方法将用户信息存入数据库
        // DAO内部会对密码进行MD5加密
        return userDao.register(user);
    }

    /**
     * 更新用户信息
     *
     * 【业务逻辑】
     * - 直接调用DAO的update方法更新用户信息
     * - 只更新传入的非null字段
     *
     * 【更新范围】
     * - 昵称（nickname）
     * - 电话（phone）
     * - 邮箱（email）
     * - 注意：不更新用户名和密码
     *
     * @param user 包含更新信息的User对象（需设置id）
     * @return true-更新成功，false-更新失败
     */
    @Override
    public boolean updateUserInfo(User user) {
        // 直接调用DAO更新用户信息
        return userDao.update(user);
    }

    /**
     * 删除用户
     *
     * 【业务逻辑】
     * - 根据用户ID删除用户记录
     * - 删除操作不可逆
     *
     * @param userId 要删除的用户的ID
     * @return true-删除成功，false-删除失败
     */
    @Override
    public boolean deleteUser(int userId) {
        // 调用DAO根据ID删除用户
        return userDao.deleteById(userId);
    }

    /**
     * 用户登录验证
     *
     * 【业务逻辑】
     * 1. 记录登录开始时间，用于性能监控
     * 2. 打印调试日志（用户名、开始时间）
     * 3. 调用DAO根据用户名查询用户信息
     * 4. 记录查询耗时
     * 5. 如果用户存在：
     *    - 对输入的密码进行MD5加密
     *    - 比对加密后的密码与数据库中的哈希值
     *    - 密码匹配返回User对象
     *    - 密码不匹配返回null
     * 6. 如果用户不存在，返回null
     * 7. 记录登录结果（成功/失败/异常）
     *
     * 【密码安全】
     * - 密码不传输明文（前端也会加密）
     * - 数据库存储MD5哈希值，而非明文
     * - 登录时比对哈希值，而非明文
     *
     * @param username 用户名
     * @param password 明文密码
     * @return 登录成功返回User对象，失败返回null
     */
    @Override
    public User login(String username, String password) {
        // 记录登录开始时间
        long startTime = System.currentTimeMillis();

        // 打印登录开始日志
        System.out.println("=== Login attempt [" + startTime + "] ===");
        System.out.println("Username: " + username);

        try {
            // 【第一步】根据用户名查询用户
            // 调用DAO的findByUsername方法从数据库查询
            User user = userDao.findByUsername(username);

            // 记录查询耗时
            System.out.println("Query completed in " + (System.currentTimeMillis() - startTime) + "ms");

            // 【第二步】处理查询结果
            if (user != null) {
                // 用户存在：进行密码验证
                System.out.println("User found: " + user.getUsername() + ", ID: " + user.getId());

                // 对输入的密码进行MD5加密
                String inputHash = MD5Util.encrypt(password);

                // 比对密码：数据库存储的哈希值 vs 输入密码的哈希值
                boolean passwordMatch = user.getPassword().equals(inputHash);
                System.out.println("Password match: " + passwordMatch);

                if (passwordMatch) {
                    // 密码匹配：登录成功
                    System.out.println("Login SUCCESS");
                    return user;
                } else {
                    // 密码不匹配：登录失败
                    System.out.println("Login FAILED - password mismatch");
                }
            } else {
                // 用户不存在：登录失败
                System.out.println("Login FAILED - user not found");
            }
        } catch (Exception e) {
            // 发生异常：记录异常信息
            System.out.println("Login FAILED - exception: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        // 打印登录结束日志
        System.out.println("=== Login attempt [" + startTime + "] ended ===");

        // 登录失败，返回null
        return null;
    }

    /**
     * 验证用户是否为管理员
     *
     * 【业务逻辑】
     * - 检查User对象的role字段是否为"admin"
     * - 使用短路求值避免空指针异常
     *
     * 【权限说明】
     * - admin: 管理员，拥有管理权限
     * - user: 普通用户，只有基本访问权限
     *
     * @param user 要检查的User对象
     * @return true-是管理员，false-是普通用户或user为null
     */
    @Override
    public boolean isAdmin(User user) {
        // 使用短路求值：user不为null 且 role等于"admin"
        return user != null && "admin".equals(user.getRole());
    }

    /**
     * 根据ID查找用户
     *
     * 【业务逻辑】
     * - 直接调用DAO的selectById方法
     * - 根据用户ID查询完整用户信息
     *
     * @param userId 用户的数字ID
     * @return 找到返回User对象，未找到返回null
     */
    @Override
    public User findById(int userId) {
        // 调用DAO根据ID查询用户
        return userDao.selectById(userId);
    }
}
