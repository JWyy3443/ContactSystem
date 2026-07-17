package com.contacts.service;

import com.contacts.entity.User;

/**
 * 用户服务接口
 *
 * 【接口说明】
 * - 定义用户相关的业务逻辑方法
 * - 由 UserServiceImpl 类实现具体逻辑
 * - 采用接口分离设计，便于维护和扩展
 *
 * 【主要功能】
 * - 用户注册：将新用户信息存入数据库
 * - 用户登录：验证用户名和密码
 * - 用户信息更新：修改用户的个人资料
 * - 用户删除：删除指定用户
 * - 用户查询：根据ID查找用户
 * - 权限验证：判断用户是否为管理员
 *
 * 【设计原则】
 * - 接口隔离：只定义用户相关的业务方法
 * - 单一职责：每个方法只负责一种业务操作
 * - 依赖倒置：Service层依赖接口，不直接依赖DAO实现
 */
public interface UserService {

    /**
     * 注册新用户
     *
     * 【功能】将新用户信息存入数据库
     * 【前置条件】用户名不能已存在
     * 【后置条件】数据库中添加新用户记录
     *
     * @param user 包含注册信息的User对象（需设置username、password等）
     * @return true-注册成功，false-注册失败（用户名已存在或其他原因）
     */
    boolean register(User user);

    /**
     * 更新用户信息
     *
     * 【功能】修改用户的个人资料（昵称、电话、邮箱）
     * 【限制】不修改密码，如需修改密码应使用单独的接口
     *
     * @param user 包含更新信息的User对象（需设置id和要更新的字段）
     * @return true-更新成功，false-更新失败（用户不存在或其他原因）
     */
    boolean updateUserInfo(User user);

    /**
     * 删除用户
     *
     * 【功能】从数据库中删除指定用户
     * 【警告】此操作不可逆，删除后用户数据将无法恢复
     *
     * @param userId 要删除的用户的ID
     * @return true-删除成功，false-删除失败（用户不存在或其他原因）
     */
    boolean deleteUser(int userId);

    /**
     * 用户登录验证
     *
     * 【功能】验证用户名和密码是否匹配
     * 【验证流程】
     * 1. 根据用户名从数据库查询用户信息
     * 2. 对输入的密码进行MD5加密
     * 3. 比对加密后的密码与数据库中存储的密码哈希值
     *
     * @param username 用户名
     * @param password 明文密码（方法内部会进行MD5加密）
     * @return 登录成功返回User对象，失败返回null
     */
    User login(String username, String password);

    /**
     * 验证用户是否为管理员
     *
     * 【功能】检查用户的角色是否为admin
     *
     * @param user 要检查的User对象
     * @return true-是管理员，false-是普通用户
     */
    boolean isAdmin(User user);

    /**
     * 根据ID查找用户
     *
     * 【功能】通过用户ID从数据库查询用户信息
     *
     * @param userId 用户的数字ID
     * @return 找到返回User对象，未找到返回null
     */
    User findById(int userId);

}
