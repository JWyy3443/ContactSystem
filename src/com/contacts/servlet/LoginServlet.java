package com.contacts.servlet;

import com.contacts.entity.User;
import com.contacts.service.UserService;
import com.contacts.service.UserServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 用户登录控制器
 *
 * 【功能说明】
 * - 处理用户登录请求
 * - 验证用户名和密码
 * - 创建Session维护登录状态
 * - 返回JSON格式的登录结果
 *
 * 【API接口】
 * - URL: /api/login
 * - 方法: POST
 * - 请求体: {"username": "xxx", "password": "xxx"}
 * - 响应: {"success": true/false, "id": 1, "username": "xxx", "nickname": "xxx", "role": "user/admin"}
 *
 * 【Session管理】
 * - 登录成功后将User对象存入Session，键名为"user"
 * - 后续请求通过Session验证用户身份
 *
 * 【测试账号】
 * - 用户名: testuser
 * - 密码: 123456
 * - 登录成功后直接返回测试用户信息，不查询数据库
 */
@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    /** 用户服务层实例，用于调用业务逻辑处理登录 */
    private UserService userService = new UserServiceImpl();

    /** Gson实例，用于JSON序列化（备用，当前使用字符串拼接） */
    private Gson gson = new Gson();

    /**
     * 处理POST登录请求
     *
     * 【处理流程】
     * 1. 设置响应格式为JSON，字符编码为UTF-8
     * 2. 读取请求体中的JSON数据
     * 3. 解析用户名和密码
     * 4. 验证测试账号（testuser/123456）
     * 5. 调用业务层验证真实用户
     * 6. 登录成功：将用户信息存入Session，返回成功JSON
     * 7. 登录失败：返回401状态码和错误信息
     *
     * @param req HTTP请求对象，包含登录信息
     * @param resp HTTP响应对象，用于返回JSON结果
     * @throws ServletException Servlet处理异常
     * @throws IOException IO读写异常
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 【第一步】设置响应格式：JSON类型，UTF-8字符编码
        // 确保返回给前端的中文字符能正确显示
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // 【第二步】读取请求体数据
            // 使用StringBuilder累加读取到的每一行数据
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // 将读取的请求体转换为字符串
            String requestBody = sb.toString();

            // 【第三步】解析JSON请求体，提取用户名和密码
            JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
            // 使用三元表达式确保null值处理，避免抛出空指针异常
            String username = jsonObject.has("username") ? jsonObject.get("username").getAsString() : null;
            String password = jsonObject.has("password") ? jsonObject.get("password").getAsString() : null;

            // 【第四步】测试账号验证
            // 如果用户名和密码匹配测试账号，直接返回成功（不查询数据库）
            if ("testuser".equals(username) && "123456".equals(password)) {
                // 创建测试用户对象
                User testUser = new User();
                testUser.setId(1);
                testUser.setUsername(username);
                testUser.setNickname("Test User");
                testUser.setRole("user");

                // 将测试用户存入Session，用于保持登录状态
                req.getSession().setAttribute("user", testUser);

                // 返回登录成功的JSON响应
                resp.getWriter().write("{\"id\":1,\"username\":\"testuser\",\"nickname\":\"Test User\",\"role\":\"user\",\"success\":true}");
                return; // 直接返回，不再执行后续数据库验证
            }

            // 【第五步】真实用户登录验证
            try {
                // 打印日志：记录登录请求开始，用于调试和问题排查
                System.out.println("=== Login Request [" + System.currentTimeMillis() + "] ===");
                System.out.println("Session ID: " + req.getSession().getId());
                System.out.println("Session user before login: " + req.getSession().getAttribute("user"));

                // 调用业务层userService的login方法进行用户验证
                // login方法内部会：
                // 1. 根据用户名查询数据库获取用户信息
                // 2. 将输入的密码进行MD5加密
                // 3. 比对数据库中的密码哈希值
                // 4. 返回User对象或null
                User dbUser = userService.login(username, password);

                // 【第六步】处理登录结果
                if (dbUser != null) {
                    // 登录成功：将用户信息存入Session
                    req.getSession().setAttribute("user", dbUser);
                    System.out.println("Session user after login: " + req.getSession().getAttribute("user"));

                    // 构建并返回成功响应的JSON字符串
                    // 包含用户ID、用户名、昵称、角色等信息
                    // 使用三元表达式处理可能的null值，避免显示"null"字符串
                    String successJson = "{\"id\":" + dbUser.getId() +
                            ",\"username\":\"" + dbUser.getUsername() +
                            "\",\"nickname\":\"" + (dbUser.getNickname() != null ? dbUser.getNickname() : "") +
                            "\",\"role\":\"" + (dbUser.getRole() != null ? dbUser.getRole() : "user") +
                            "\",\"success\":true}";
                    resp.getWriter().write(successJson);
                } else {
                    // 登录失败：用户名不存在或密码错误
                    // 返回401状态码（未授权）
                    resp.setStatus(401);
                    resp.getWriter().write("{\"success\":false,\"error\":\"用户名或密码错误\"}");
                }
                System.out.println("=== Login Request ended ===");

            } catch (Exception dbEx) {
                // 【第七步】登录过程异常处理
                // 捕获业务层抛出的异常，如数据库连接失败等
                System.err.println("Login exception: " + dbEx.getClass().getName() + ": " + dbEx.getMessage());
                dbEx.printStackTrace();

                // 返回500状态码（服务器内部错误）
                resp.setStatus(500);
                resp.getWriter().write("{\"success\":false,\"error\":\"系统错误: " + dbEx.getClass().getSimpleName() + "\"}");
            }

        } catch (Exception e) {
            // 【第八步】请求解析异常处理
            // 如JSON格式错误、请求体为空等情况
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }
}
