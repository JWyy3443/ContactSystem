package com.contacts.entity;

/**
 * 用户实体类
 *
 * 【类说明】
 * - 对应数据库中的 t_user 表
 * - 用于在各层之间传递用户数据
 * - 是一个简单的POJO（Plain Old Java Object）类
 *
 * 【数据库表结构】
 * t_user 表字段：
 * - id: 用户ID（主键，自增）
 * - username: 用户名（唯一）
 * - password: 密码（MD5加密后存储）
 * - nickname: 昵称
 * - phone: 电话
 * - email: 邮箱
 * - role: 角色（user=普通用户，admin=管理员）
 * - reg_time: 注册时间
 *
 * 【使用场景】
 * - Servlet层：接收前端请求数据
 * - Service层：处理业务逻辑
 * - Dao层：数据库操作的结果封装
 * - 前端：JSON序列化和反序列化
 *
 * 【字段说明】
 * - id: 用户唯一标识，Integer类型允许为null（新用户未插入数据库前）
 * - username: 登录账号，唯一标识
 * - password: 登录密码，存储MD5哈希值，永不明文
 * - nickname: 显示名称，可自定义
 * - phone: 联系电话，可选
 * - email: 电子邮箱，可选
 * - role: 用户角色，默认为"user"，管理员为"admin"
 * - regTime: 注册时间，String类型存储时间戳
 */
public class User {

    /** 用户ID（主键），数据库自增，新用户为null */
    private Integer id;

    /** 用户名（登录账号），唯一标识 */
    private String username;

    /** 密码（MD5加密后的哈希值），永不存储明文 */
    private String password;

    /** 昵称（显示名称），可选 */
    private String nickname;

    /** 电话号码，可选 */
    private String phone;

    /** 电子邮箱，可选 */
    private String email;

    /** 用户角色：user=普通用户，admin=管理员 */
    private String role;

    /** 注册时间，String类型存储时间戳 */
    private String regTime;

    /**
     * 默认无参构造函数
     *
     * 【用途】
     * - 是JavaBean必须的构造方法
     * - 用于反序列化创建对象
     * - 用于ORM框架反射创建对象
     */
    public User() {
    }

    /**
     * 带参构造函数
     *
     * 【用途】
     * - 快速创建用户对象
     * - 设置基本注册信息
     * - 角色默认为"user"
     *
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称
     * @param phone 电话
     * @param email 邮箱
     */
    public User(String username, String password, String nickname, String phone, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.role = "user"; // 默认角色为普通用户
    }

    // ==================== Getter 和 Setter 方法 ====================

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public Integer getId() { return id; }

    /**
     * 设置用户ID
     * @param id 用户ID
     */
    public void setId(Integer id) { this.id = id; }

    /**
     * 获取用户名
     * @return 用户名
     */
    public String getUsername() { return username; }

    /**
     * 设置用户名
     * @param username 用户名
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * 获取密码（MD5哈希值）
     * @return 密码哈希值
     */
    public String getPassword() { return password; }

    /**
     * 设置密码（应传入MD5加密后的值）
     * @param password 密码哈希值
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * 获取昵称
     * @return 昵称
     */
    public String getNickname() { return nickname; }

    /**
     * 设置昵称
     * @param nickname 昵称
     */
    public void setNickname(String nickname) { this.nickname = nickname; }

    /**
     * 获取电话
     * @return 电话号码
     */
    public String getPhone() { return phone; }

    /**
     * 设置电话
     * @param phone 电话号码
     */
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * 获取邮箱
     * @return 电子邮箱
     */
    public String getEmail() { return email; }

    /**
     * 设置邮箱
     * @param email 电子邮箱
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * 获取角色
     * @return 角色（user或admin）
     */
    public String getRole() { return role; }

    /**
     * 设置角色
     * @param role 角色
     */
    public void setRole(String role) { this.role = role; }

    /**
     * 获取注册时间
     * @return 注册时间字符串
     */
    public String getRegTime() { return regTime; }

    /**
     * 设置注册时间
     * @param regTime 注册时间字符串
     */
    public void setRegTime(String regTime) { this.regTime = regTime; }

    /**
     * 重写toString方法
     *
     * 【用途】
     * - 方便调试和日志输出
     * - 显示用户的主要信息
     *
     * @return 用户信息的字符串表示
     */
    @Override
    public String toString() {
        return "ID: " + id + ", 用户名: " + username + ", 昵称: " + nickname +
                ", 电话: " + phone + ", 邮箱: " + email + ", 角色: " + role +
                ", 注册时间: " + regTime;
    }
}
