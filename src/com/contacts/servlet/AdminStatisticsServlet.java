package com.contacts.servlet;

import com.contacts.entity.User;
import com.contacts.service.AdminService;
import com.contacts.service.AdminServiceImpl;
import com.contacts.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 管理员统计信息控制器
 *
 * 【功能说明】
 * - 获取系统的用户统计数据
 * - 包括：用户总数、管理员数量、普通用户数量
 * - 仅限管理员访问
 *
 * 【API接口】
 * - URL: /api/admin/statistics
 * - 方法: GET
 * - 权限要求: 必须登录且角色为admin
 * - 响应: {"totalUsers":10,"adminCount":2,"userCount":8}
 *
 * 【统计数据】
 * - totalUsers: 系统中的用户总数量
 * - adminCount: 角色为admin的用户数量
 * - userCount: 角色为user（普通用户）的数量
 *
 * 【权限控制】
 * - 从Session中获取当前登录用户
 * - 验证用户是否登录
 * - 验证用户角色是否为admin
 * - 未授权用户返回403 Forbidden
 */
@WebServlet("/api/admin/statistics")
public class AdminStatisticsServlet extends HttpServlet {

    /** 管理员服务层实例，用于调用业务逻辑获取统计数据 */
    private AdminService adminService = new AdminServiceImpl();

    /** Gson实例，用于将统计结果序列化为JSON字符串 */
    private Gson gson = GsonUtil.getInstance();

    /**
     * 处理GET请求，获取用户统计信息
     *
     * 【处理流程】
     * 1. 设置响应格式为JSON，字符编码为UTF-8
     * 2. 从Session中获取当前登录用户
     * 3. 验证用户权限（必须是已登录的管理员）
     * 4. 调用业务层获取用户总数
     * 5. 调用业务层获取按角色分组的用户数量
     * 6. 构建JSON响应对象，包含各项统计数据
     * 7. 将统计结果序列化为JSON并返回
     *
     * 【数据来源】
     * - getTotalUserCount(): 统计t_user表中的总记录数
     * - getUserCountByRole(): SQL GROUP BY role统计各角色数量
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
        User currentUser = (User) req.getSession().getAttribute("user");

        // 【第三步】验证用户权限
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            // 未授权：返回403 Forbidden状态码
            resp.setStatus(403);
            resp.getWriter().write("{\"error\":\"Permission denied\"}");
            return;
        }

        // 【第四步】获取用户总数
        // 统计t_user表中的所有记录数量
        int totalUsers = adminService.getTotalUserCount();

        // 【第五步】获取按角色分组的用户数量
        // 返回Map，key为角色名称（admin/user），value为用户数量
        Map<String, Integer> roleCount = adminService.getUserCountByRole();

        // 【第六步】构建统计结果JSON对象
        JsonObject result = new JsonObject();

        // 添加用户总数
        result.addProperty("totalUsers", totalUsers);

        // 添加管理员数量
        // 使用getOrDefault避免key不存在时返回null
        result.addProperty("adminCount", roleCount.getOrDefault("admin", 0));

        // 添加普通用户数量
        result.addProperty("userCount", roleCount.getOrDefault("user", 0));

        // 【第七步】序列化为JSON并返回
        resp.getWriter().write(gson.toJson(result));
    }
}
