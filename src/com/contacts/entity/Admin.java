package com.contacts.entity;

/**
 * 管理员实体类
 *
 * 【类说明】
 * - 继承自User类
 * - 表示系统管理员用户
 * - 扩展了管理员级别属性
 *
 * 【设计意图】
 * - Admin是User的子类，继承了用户的所有属性
 * - 在此基础上扩展了管理员特有的属性（如管理员级别）
 * - 保持了代码的继承层次结构
 *
 * 【与User的区别】
 * - role固定为"admin"
 * - 增加了adminLevel管理员级别属性
 * - 构造方法自动设置role为admin
 *
 * 【使用场景】
 * - 管理员登录时创建Admin对象
 * - 区分普通用户和管理员用户
 * - 未来可扩展更细粒度的权限管理
 */
public class Admin extends User {

    /** 管理员级别，用于更细粒度的权限管理（目前固定为"super"） */
    private String adminLevel = "super";

    /**
     * 默认构造函数
     *
     * 【功能】
     * - 调用父类无参构造方法
     * - 自动设置role为"admin"
     */
    public Admin() {
        super();
        setRole("admin");
    }

    /**
     * 带参构造函数
     *
     * 【功能】
     * - 创建指定用户名和密码的管理员
     * - 自动设置昵称为"管理员"
     * - 自动设置role为"admin"
     *
     * @param username 用户名
     * @param password 密码
     */
    public Admin(String username, String password) {
        // 调用父类构造方法，设置基本信息和默认昵称
        super(username, password, "管理员", null, null);
        // 强制设置角色为admin
        setRole("admin");
    }

    /**
     * 获取管理员级别
     * @return 管理员级别
     */
    public String getAdminLevel() {
        return adminLevel;
    }

    /**
     * 设置管理员级别
     * @param adminLevel 管理员级别
     */
    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }
}
