package com.contacts.servlet;

import com.contacts.entity.User;
import com.contacts.service.AdminService;
import com.contacts.service.AdminServiceImpl;
import com.contacts.util.GsonUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 管理员用户列表控制器
 *
 * 【功能说明】
 * - 获取所有用户的列表信息
 * - 仅限管理员访问
 * - 返回所有用户的基本信息（密码除外）
 *
 * 【API接口】
 * - URL: /api/admin/users
 * - 方法: GET
 * - 权限要求: 必须登录且角色为admin
 * - 响应: [{"id":1,"username":"xxx","nickname":"xxx","role":"xxx"},...]
 *
 * 【权限控制】
 * - 从Session中获取当前登录用户
 * - 验证用户是否登录
 * - 验证用户角色是否为admin
 * - 未授权用户返回403 Forbidden
 *
 * 【数据处理】
 * - 查询所有用户列表
 * - 遍历列表，清除每个用户的密码字段
 * - 使用Gson将用户列表序列化为JSON
 */
@WebServlet("/api/admin/users")
public class AdminUserListServlet extends HttpServlet {

    /** 管理员服务层实例，用于调用业务逻辑获取用户列表 */
    private AdminService adminService = new AdminServiceImpl();

    /** Gson实例，用于将用户列表序列化为JSON字符串 */
    private Gson gson = GsonUtil.getInstance();

    /**
     * 处理GET请求，获取所有用户列表
     *
     * 【处理流程】
     * 1. 设置响应格式为JSON，字符编码为UTF-8
     * 2. 从Session中获取当前登录用户
     * 3. 验证用户权限（必须是已登录的管理员）
     * 4. 调用业务层获取所有用户列表
     * 5. 清除所有用户的密码字段
     * 6. 将用户列表序列化为JSON并返回
     *
     * 【权限验证】
     * - currentUser == null: 用户未登录
     * - !"admin".equals(currentUser.getRole()): 用户不是管理员
     * - 两种情况都返回403 Forbidden，禁止访问
     *
     * @param req HTTP请求对象
     * @param resp HTTP响应对象
     * @throws ServletException Servlet处理异常
     * @throws IOException IO读写异常
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 【第一步】设置响应格式：JSON类型，UTF-8字符编码
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // 【第二步】从Session中获取当前登录用户
        // Session中存储的"user"属性是登录时存入的User对象
        User currentUser = (User) req.getSession().getAttribute("user");

        // 【第三步】验证用户权限
        // 检查用户是否已登录且角色为admin
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            // 未授权：返回403 Forbidden状态码
            resp.setStatus(403);
            resp.getWriter().write("{\"error\":\"Permission denied\"}");
            return; // 直接返回，不继续处理
        }

        // 【第四步】获取所有用户列表
        // 调用adminService的getAllUsers方法查询数据库
        List<User> users = adminService.getAllUsers();

        // 【第五步】清除密码字段
        // 安全起见，不将用户密码返回给客户端
        // 遍历用户列表，将每个用户的密码设置为null
        for (User u : users) {
            u.setPassword(null);
        }

        // 【第六步】序列化为JSON并返回
        // 使用Gson将用户列表转换为JSON数组字符串
        resp.getWriter().write(gson.toJson(users));
    }
}
