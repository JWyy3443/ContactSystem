package com.contacts.entity;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String role;      // "user" or "admin"
    private String regTime;

    // ���췽��
    public User() {
    }

    public User(String username, String password, String nickname, String phone, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.role = "user";
    }

    // Getter �� Setter����װ��
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getRegTime() { return regTime; }
    public void setRegTime(String regTime) { this.regTime = regTime; }

    @Override
    public String toString() {
        return "ID: " + id + ", �û���: " + username + ", �ǳ�: " + nickname +
                ", �绰: " + phone + ", ����: " + email + ", ��ɫ: " + role +
                ", ע��ʱ��: " + regTime;
    }
}