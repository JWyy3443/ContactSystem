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
 * 管理员用户搜索控制器
 *
 * 【功能说明】
 * - 根据关键词搜索用户
 * - 支持用户名和昵称的模糊搜索
 * - 仅限管理员访问
 *
 * 【API接口】
 * - URL: /api/admin/search?keyword=xxx
 * - 方法: GET
 * - 参数: keyword - 搜索关键词
 * - 权限要求: 必须登录且角色为admin
 * - 响应: [{"id":1,"username":"xxx","nickname":"xxx","role":"xxx"},...]
 *
 * 【搜索逻辑】
 * - 使用SQL的LIKE语句进行模糊匹配
 * - 搜索范围：用户名（username）、昵称（nickname）
 * - 关键词会被前后加上%实现模糊匹配
 * - 例如：keyword="张" 会匹配所有用户名或昵称包含"张"的用户
 *
 * 【权限控制】
 * - 从Session中获取当前登录用户
 * - 验证用户是否登录
 * - 验证用户角色是否为admin
 * - 未授权用户返回403 Forbidden
 */
@WebServlet("/api/admin/search")
public class AdminSearchServlet extends HttpServlet {

    /** 管理员服务层实例，用于调用业务逻辑搜索用户 */
    private AdminService adminService = new AdminServiceImpl();

    /** Gson实例，用于将用户列表序列化为JSON字符串 */
    private Gson gson = GsonUtil.getInstance();

    /**
     * 处理GET请求，根据关键词搜索用户
     *
     * 【处理流程】
     * 1. 设置响应格式为JSON，字符编码为UTF-8
     * 2. 从Session中获取当前登录用户
     * 3. 验证用户权限（必须是已登录的管理员）
     * 4. 从请求参数中获取搜索关键词
     * 5. 调用业务层执行用户搜索
     * 6. 清除所有用户的密码字段
     * 7. 将用户列表序列化为JSON并返回
     *
     * 【参数获取】
     * - req.getParameter("keyword") 从URL查询参数中获取关键词
     * - 例如：/api/admin/search?keyword=test 时，keyword="test"
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

        // 【第四步】获取搜索关键词
        // 从URL查询参数中获取keyword参数
        // 例如：/api/admin/search?keyword=张三 时，keyword="张三"
        String keyword = req.getParameter("keyword");

        // 【第五步】调用业务层搜索用户
        // searchUsers方法会：
        // 1. 使用SQL LIKE语句模糊匹配用户名和昵称
        // 2. 返回匹配的用户列表
        List<User> users = adminService.searchUsers(keyword);

        // 【第六步】清除密码字段
        for (User u : users) {
            u.setPassword(null);
        }

        // 【第七步】序列化为JSON并返回
        resp.getWriter().write(gson.toJson(users));
    }
}
