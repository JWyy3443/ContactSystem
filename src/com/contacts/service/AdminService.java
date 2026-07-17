package com.contacts.service;

import com.contacts.entity.User;
import java.util.List;
import java.util.Map;

/**
 * 管理员服务接口
 *
 * 【接口说明】
 * - 定义管理员相关的业务逻辑方法
 * - 由 AdminServiceImpl 类实现具体逻辑
 * - 提供用户管理和统计功能
 *
 * 【主要功能】
 * - 获取所有用户列表
 * - 搜索用户（支持用户名和昵称模糊匹配）
 * - 打印统计信息到控制台
 * - 获取用户总数
 * - 按角色统计用户数量
 *
 * 【使用场景】
 * - 管理员后台管理界面
 * - 用户数据统计
 * - 管理员控制台
 *
 * 【权限说明】
 * - 所有方法仅限管理员调用
 * - 调用前需验证用户角色为admin
 */
public interface AdminService {

    /**
     * 获取所有用户列表
     *
     * 【功能】查询数据库中的所有用户
     * 【返回】包含所有用户的List集合
     *
     * @return 包含所有User对象的List集合
     */
    List<User> getAllUsers();

    /**
     * 搜索用户
     *
     * 【功能】根据关键词模糊搜索用户
     * 【搜索范围】用户名（username）、昵称（nickname）
     * 【匹配方式】SQL LIKE语句，前后加%实现模糊匹配
     *
     * @param keyword 搜索关键词
     * @return 匹配的用户List集合
     */
    List<User> searchUsers(String keyword);

    /**
     * 打印统计信息
     *
     * 【功能】将用户统计数据打印到控制台
     * 【输出内容】用户总数、各角色用户数量等
     *
     * 【使用场景】
     * - 管理员登录时显示欢迎信息
     * - 系统健康检查
     */
    void printStatistics();

    /**
     * 获取用户总数
     *
     * 【功能】统计数据库中的用户总数量
     * 【计算方式】SELECT COUNT(*) FROM t_user
     *
     * @return 用户总数（整数）
     */
    int getTotalUserCount();

    /**
     * 按角色统计用户数量
     *
     * 【功能】按用户角色分组统计数量
     * 【角色分类】admin（管理员）、user（普通用户）
     * 【返回格式】Map，key为角色名，value为用户数量
     *
     * @return 包含角色名称和对应数量的Map
     */
    Map<String, Integer> getUserCountByRole();

}
