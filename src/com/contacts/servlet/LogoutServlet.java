package com.contacts.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户退出登录控制器
 *
 * 【功能说明】
 * - 处理用户退出登录请求
 * - 销毁当前用户的Session
 * - 重定向到登录页面
 *
 * 【API接口】
 * - URL: /logout
 * - 方法: GET
 * - 响应: 重定向到 /index.jsp
 *
 * 【退出流程】
 * 1. 调用Session.invalidate()销毁当前会话
 * 2. 重定向到登录页面index.jsp
 *
 * 【Session销毁】
 * - invalidate()会立即销毁当前Session
 * - 销毁后Session中的所有数据都会丢失
 * - 后续请求将创建新的Session
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * 处理GET退出登录请求
     *
     * 【处理流程】
     * 1. 调用req.getSession().invalidate()销毁当前Session
     *    - 这会立即终止当前会话
     *    - 清除Session中存储的所有数据（如登录用户信息）
     * 2. 调用resp.sendRedirect()重定向到登录页面
     *    - 用户访问/logout后，浏览器会跳转到index.jsp
     *
     * 【安全说明】
     * - 退出登录后，之前Session中存储的用户信息立即失效
     * - 需要重新登录才能访问受保护的资源
     *
     * @param req HTTP请求对象
     * @param resp HTTP响应对象，用于重定向
     * @throws ServletException Servlet处理异常
     * @throws IOException IO读写异常
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 【第一步】销毁当前用户的Session
        // invalidate()会：
        // 1. 立即终止当前会话
        // 2. 清除会话中存储的所有属性数据
        // 3. 使当前Session ID失效
        req.getSession().invalidate();

        // 【第二步】重定向到登录页面
        // 使用sendRedirect实现客户端重定向
        // 用户浏览器地址栏会变为项目根路径/index.jsp
        // req.getContextPath()获取当前Web应用的根路径
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}
