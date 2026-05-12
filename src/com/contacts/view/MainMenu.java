package com.contacts.view;

import com.contacts.entity.Admin;
import com.contacts.entity.User;
import com.contacts.service.AdminService;
import com.contacts.service.AdminServiceImpl;
import com.contacts.service.UserService;
import com.contacts.service.UserServiceImpl;

import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private static UserService userService = new UserServiceImpl();
    private static AdminService adminService = new AdminServiceImpl();
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args) {
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                if (userService.isAdmin(currentUser)) {
                    showAdminMenu();
                } else {
                    showUserMenu();
                }
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n========== 通讯录系统 ==========");
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.println("3. 退出");
        System.out.print("请选择: ");
        int choice = readInt();
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                System.out.println("感谢使用，再见！");
                System.exit(0);
                break;
            default:
                System.out.println("无效输入");
        }
    }

    private static void login() {
        System.out.print("用户名: ");
        String username = scanner.nextLine();
        System.out.print("密码: ");
        String password = scanner.nextLine();
        User user = userService.login(username, password);
        if (user != null) {
            currentUser = user;
            System.out.println("登录成功！欢迎 " + user.getNickname());
        } else {
            System.out.println("用户名或密码错误");
        }
    }

    private static void register() {
        System.out.print("用户名: ");
        String username = scanner.nextLine();
        System.out.print("密码: ");
        String password = scanner.nextLine();
        System.out.print("昵称: ");
        String nickname = scanner.nextLine();
        System.out.print("电话: ");
        String phone = scanner.nextLine();
        System.out.print("邮箱: ");
        String email = scanner.nextLine();

        User newUser = new User(username, password, nickname, phone, email);
        if (userService.register(newUser)) {
            System.out.println("注册成功！请登录。");
        } else {
            System.out.println("注册失败，用户名可能已存在。");
        }
    }

    // 普通用户菜单
    private static void showUserMenu() {
        System.out.println("\n===== 用户菜单 =====");
        System.out.println("1. 查看我的信息");
        System.out.println("2. 修改我的信息");
        System.out.println("3. 注销账号");
        System.out.println("4. 退出登录");
        System.out.print("请选择: ");
        int choice = readInt();
        switch (choice) {
            case 1:
                showMyInfo();
                break;
            case 2:
                updateMyInfo();
                break;
            case 3:
                deleteMyAccount();
                break;
            case 4:
                currentUser = null;
                System.out.println("已退出登录");
                break;
            default:
                System.out.println("无效选择");
        }
    }

    private static void showMyInfo() {
        System.out.println("\n您的信息:");
        System.out.println(currentUser);
    }

    private static void updateMyInfo() {
        System.out.println("修改信息（留空表示不修改）:");
        System.out.print("新昵称 (" + currentUser.getNickname() + "): ");
        String nickname = scanner.nextLine();
        if (!nickname.trim().isEmpty()) currentUser.setNickname(nickname);

        System.out.print("新电话 (" + currentUser.getPhone() + "): ");
        String phone = scanner.nextLine();
        if (!phone.trim().isEmpty()) currentUser.setPhone(phone);

        System.out.print("新邮箱 (" + currentUser.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.trim().isEmpty()) currentUser.setEmail(email);

        if (userService.updateUserInfo(currentUser)) {
            System.out.println("信息修改成功！");
        } else {
            System.out.println("修改失败");
        }
    }

    private static void deleteMyAccount() {
        System.out.print("确认注销账号？此操作不可恢复 (y/n): ");
        String confirm = scanner.nextLine();
        if ("y".equalsIgnoreCase(confirm)) {
            if (userService.deleteUser(currentUser.getId())) {
                System.out.println("账号已注销");
                currentUser = null;
            } else {
                System.out.println("注销失败");
            }
        } else {
            System.out.println("已取消");
        }
    }

    // 管理员菜单
    private static void showAdminMenu() {
        System.out.println("\n===== 管理员菜单 =====");
        System.out.println("1. 查看所有用户");
        System.out.println("2. 查询用户");
        System.out.println("3. 统计信息");
        System.out.println("4. 退出登录");
        System.out.print("请选择: ");
        int choice = readInt();
        switch (choice) {
            case 1:
                listAllUsers();
                break;
            case 2:
                searchUsers();
                break;
            case 3:
                adminService.printStatistics();
                break;
            case 4:
                currentUser = null;
                System.out.println("已退出管理员登录");
                break;
            default:
                System.out.println("无效选择");
        }
    }

    private static void listAllUsers() {
        List<User> users = adminService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("暂无用户");
        } else {
            System.out.println("\n====== 所有用户列表 ======");
            for (User u : users) {
                System.out.println(u);
            }
            System.out.println("总数: " + users.size());
        }
    }

    private static void searchUsers() {
        System.out.print("请输入搜索关键词（用户名或昵称）: ");
        String keyword = scanner.nextLine();
        List<User> results = adminService.searchUsers(keyword);
        if (results.isEmpty()) {
            System.out.println("未找到匹配的用户");
        } else {
            System.out.println("\n====== 搜索结果 ======");
            for (User u : results) {
                System.out.println(u);
            }
        }
    }

    private static int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}