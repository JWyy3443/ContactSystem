package com.contacts.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 测试接口控制器
 *
 * 【功能说明】
 * - 提供一个简单的测试接口
 * - 用于验证API是否正常工作
 * - 检查数据库连接是否正常
 *
 * 【API接口】
 * - URL: /api/test
 * - 方法: GET
 * - 响应: {"status":"ok","message":"Test API is working"}
 *
 * 【使用场景】
 * - 前后端联调时，用于验证后端服务是否正常
 * - 排查问题时，用于判断请求是否能到达后端
 * - 部署验证，确认服务已成功启动
 *
 * 【注意事项】
 * - 无需登录即可访问
 * - 不涉及任何业务逻辑
 * - 不会访问数据库
 */
@WebServlet("/api/test")
public class TestServlet extends HttpServlet {

    /**
     * 处理GET测试请求
     *
     * 【处理流程】
     * 1. 设置响应格式为JSON，字符编码为UTF-8
     * 2. 直接返回一个固定的JSON响应
     *
     * 【返回数据】
     * - status: "ok" 表示服务正常
     * - message: "Test API is working" 提示信息
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

        // 【第二步】返回测试成功的JSON响应
        // 这个接口不做任何业务处理，只是简单地返回一个成功响应
        // 如果能收到这个响应，说明：
        // 1. Tomcat服务正常
        // 2. Servlet映射正确
        // 3. 网络通信正常
        resp.getWriter().write("{\"status\":\"ok\",\"message\":\"Test API is working\"}");
    }
}
