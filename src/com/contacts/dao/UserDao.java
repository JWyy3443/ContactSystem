package com.contacts.dao;

import com.contacts.entity.User;
import java.util.List;

/**
 * 用户数据访问接口
 *
 * 【接口说明】
 * - 定义用户数据的数据库操作方法
 * - 由 UserDaoImpl 类实现具体逻辑
 * - 采用接口分离设计，便于数据访问层独立维护和测试
 *
 * 【主要功能】
 * - 用户注册（INSERT）
 * - 用户信息更新（UPDATE）
 * - 用户删除（DELETE）
 * - 用户查询（SELECT）
 *
 * 【设计原则】
 * - 面向接口编程：Service层依赖接口，不直接依赖实现
 * - 单一职责：每个方法只负责一种数据库操作
 * - 资源管理：数据库连接、Statement、ResultSet的正确关闭由实现类负责
 *
 * 【注意事项】
 * - 所有方法抛出SQLException，由调用者处理
 * - 密码在注册时已加密，查询返回的是加密后的字符串
 */
public interface UserDao {

    /**
     * 注册新用户
     *
     * 【SQL操作】INSERT INTO t_user ...
     * 【功能】将新用户信息插入数据库
     *
     * 【参数要求】
     * - user.getUsername(): 用户名（唯一）
     * - user.getPassword(): 密码（MD5加密后的字符串）
     * - user.getNickname(): 昵称（可选）
     * - user.getPhone(): 电话（可选）
     * - user.getEmail(): 邮箱（可选）
     *
     * @param user 包含注册信息的User对象
     * @return true-插入成功，false-插入失败
     */
    boolean register(User user);

    /**
     * 更新用户信息
     *
     * 【SQL操作】UPDATE t_user SET ...
     * 【功能】修改用户的个人资料
     *
     * 【更新字段】
     * - nickname: 昵称
     * - phone: 电话
     * - email: 邮箱
     *
     * 【注意】不更新用户名和密码
     *
     * @param user 包含更新信息的User对象（需设置id）
     * @return true-更新成功，false-更新失败
     */
    boolean update(User user);

    /**
     * 根据ID删除用户
     *
     * 【SQL操作】DELETE FROM t_user WHERE id=?
     * 【功能】删除指定ID的用户记录
     *
     * 【危险操作】此操作不可逆，删除后数据无法恢复
     *
     * @param userId 要删除的用户的ID
     * @return true-删除成功，false-删除失败
     */
    boolean deleteById(int userId);

    /**
     * 根据用户名查询用户
     *
     * 【SQL操作】SELECT * FROM t_user WHERE username=?
     * 【功能】通过用户名精确查找用户
     *
     * 【使用场景】
     * - 登录验证
     * - 检查用户名是否已存在
     *
     * @param username 用户名
     * @return 找到返回User对象，未找到返回null
     */
    User findByUsername(String username);

    /**
     * 根据ID查询用户
     *
     * 【SQL操作】SELECT * FROM t_user WHERE id=?
     * 【功能】通过ID精确查找用户
     *
     * 【使用场景】
     * - 查找特定用户信息
     * - 用户详情页面
     *
     * @param id 用户ID
     * @return 找到返回User对象，未找到返回null
     */
    User findById(int id);

    /**
     * 根据ID查询用户
     *
     * 【SQL操作】SELECT * FROM t_user WHERE id=?
     * 【功能】通过ID精确查找用户
     *
     * 【说明】与findById功能相同，可能是为了兼容不同的命名习惯
     *
     * @param userId 用户ID
     * @return 找到返回User对象，未找到返回null
     */
    User selectById(int userId);

}
