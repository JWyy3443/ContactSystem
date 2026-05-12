package com.contacts.entity;

public class Admin extends User {
    // 管理员特有的权限，比如可以拥有更高的操作级别
    // 这里可以扩展特有属性，如权限等级
    private String adminLevel = "super";

    public Admin() {
        super();
        setRole("admin");
    }

    public Admin(String username, String password) {
        super(username, password, "管理员", null, null);
        setRole("admin");
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }
}