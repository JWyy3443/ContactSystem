package com.contacts.dao;

import com.contacts.entity.User;
import java.util.List;
import java.util.Map;

/**
 * 管理员数据访问接口
 *
 * 【接口说明】
 * - 定义管理员相关的数据查询方法
 * - 由 AdminDaoImpl 类实现具体逻辑
 * - 提供用户管理和统计功能的数据支持
 *
 * 【主要功能】
 * - 查询所有用户列表
 * - 搜索用户（模糊匹配）
 * - 按角色统计用户数量
 * - 获取用户总数
 *
 * 【使用场景】
 * - 管理员后台管理界面数据来源
 * - 用户统计报表
 * - 管理员控制台
 *
 * 【注意事项】
 * - 所有方法抛出SQLException，由调用者处理
 * - 返回的用户列表中密码字段为空（安全性）
 */
public interface AdminDao {

    /**
     * 查询所有用户
     *
     * 【SQL操作】SELECT * FROM t_user
     * 【功能】获取数据库中所有用户记录
     *
     * @return 包含所有User对象的List集合，按ID排序
     */
    List<User> findAllUsers();

    /**
     * 搜索用户（模糊查询）
     *
     * 【SQL操作】
     * SELECT * FROM t_user
     * WHERE username LIKE '%keyword%' OR nickname LIKE '%keyword%'
     *
     * 【功能】
     * - 根据关键词搜索用户
     * - 同时匹配用户名和昵称
     * - 使用SQL LIKE语句实现模糊搜索
     *
     * @param keyword 搜索关键词
     * @return 匹配的用户List集合
     */
    List<User> searchUsers(String keyword);

    /**
     * 按角色统计用户数量
     *
     * 【SQL操作】
     * SELECT role, COUNT(*) as count
     * FROM t_user GROUP BY role
     *
     * 【功能】
     * - 按用户角色分组统计
     * - 返回每个角色的用户数量
     *
     * @return Map对象，key为角色名，value为用户数量
     */
    Map<String, Integer> getUserCountByRole();

    /**
     * 获取用户总数
     *
     * 【SQL操作】SELECT COUNT(*) FROM t_user
     * 【功能】统计数据库中的用户记录总数
     *
     * @return 用户总数（整数）
     */
    int getTotalUserCount();

}
