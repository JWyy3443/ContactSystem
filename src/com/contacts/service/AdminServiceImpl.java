package com.contacts.service;

import com.contacts.dao.AdminDao;
import com.contacts.dao.AdminDaoImpl;
import com.contacts.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 管理员服务实现类
 *
 * 【类说明】
 * - 实现 AdminService 接口定义的业务逻辑
 * - 负责处理管理员相关的业务流程
 * - 协调 DAO 层完成数据库操作
 *
 * 【依赖关系】
 * - 依赖 AdminDao 接口进行数据库操作
 *
 * 【设计模式】
 * - 采用面向接口编程，依赖注入 DAO 实现
 * - 业务逻辑与数据访问分离
 */
public class AdminServiceImpl implements AdminService {

    /** 管理员数据访问层接口，通过依赖注入获取实现 */
    private AdminDao adminDao = new AdminDaoImpl();

    /**
     * 获取所有用户列表
     *
     * 【业务逻辑】
     * - 直接调用DAO的findAllUsers方法查询所有用户
     *
     * @return 包含所有User对象的List集合
     */
    @Override
    public List<User> getAllUsers() {
        // 调用DAO查询所有用户
        return adminDao.findAllUsers();
    }

    /**
     * 搜索用户
     *
     * 【业务逻辑】
     * - 将关键词传递给DAO进行模糊搜索
     * - DAO内部使用SQL LIKE语句实现模糊匹配
     *
     * 【搜索条件】
     * - username LIKE '%keyword%'
     * - OR nickname LIKE '%keyword%'
     *
     * @param keyword 搜索关键词
     * @return 匹配的用户List集合
     */
    @Override
    public List<User> searchUsers(String keyword) {
        // 调用DAO执行用户搜索
        return adminDao.searchUsers(keyword);
    }

    /**
     * 打印统计信息到控制台
     *
     * 【功能】
     * - 统计并打印系统用户数据
     * - 输出格式化的统计报告
     *
     * 【输出内容】
     * - 总用户数
     * - 各角色的用户数量
     *
     * 【使用场景】
     * - 管理员登录系统时显示欢迎信息
     * - 管理员控制台查看数据概览
     */
    @Override
    public void printStatistics() {
        // 打印统计报告头部
        System.out.println("\n========== 系统统计信息 ==========");

        // 获取并打印用户总数
        int total = adminDao.getTotalUserCount();
        System.out.println("已注册用户数: " + total);

        // 获取并打印按角色分组的用户数
        Map<String, Integer> roleMap = adminDao.getUserCountByRole();

        // 遍历Map，打印每个角色的用户数量
        for (Map.Entry<String, Integer> entry : roleMap.entrySet()) {
            // entry.getKey(): 角色名称（admin/user）
            // entry.getValue(): 该角色的用户数量
            System.out.println("角色 [" + entry.getKey() + "] : " + entry.getValue() + " 人");
        }

        // 打印统计报告尾部
        System.out.println("==================================\n");
    }

    /**
     * 获取用户总数
     *
     * 【业务逻辑】
     * - 调用DAO统计数据库中的用户记录数
     *
     * @return 用户总数
     */
    @Override
    public int getTotalUserCount() {
        // 调用DAO获取用户总数
        return adminDao.getTotalUserCount();
    }

    /**
     * 按角色统计用户数量
     *
     * 【业务逻辑】
     * - 调用DAO执行分组统计查询
     * - 使用SQL GROUP BY按角色分组
     *
     * @return Map对象，key为角色名，value为用户数量
     */
    @Override
    public Map<String, Integer> getUserCountByRole() {
        // 调用DAO获取按角色统计的用户数量
        return adminDao.getUserCountByRole();
    }

}
