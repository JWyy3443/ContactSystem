package com.contacts.servlet;

import com.contacts.entity.User;
import com.contacts.service.UserService;
import com.contacts.service.UserServiceImpl;
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
 * 用户注册控制器
 *
 * 【功能说明】
 * - 处理新用户注册请求
 * - 验证用户名是否已存在
 * - 将新用户信息存入数据库
 * - 返回注册结果的JSON响应
 *
 * 【API接口】
 * - URL: /api/register
 * - 方法: POST
 * - 请求体: {"username": "xxx", "password": "xxx", "nickname": "xxx", "phone": "xxx", "email": "xxx"}
 * - 响应: {"success": true/false}
 *
 * 【注册流程】
 * 1. 接收并解析JSON格式的注册信息
 * 2. 验证用户名是否已存在（防止重复注册）
 * 3. 调用业务层执行数据库插入操作
 * 4. 返回注册成功/失败结果
 *
 * 【密码安全】
 * - 密码在Service层使用MD5加密后再存入数据库
 * - 数据库中存储的是密码的哈希值，而非明文
 */
@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {

    /** 用户服务层实例，处理注册相关的业务逻辑 */
    private UserService userService = new UserServiceImpl();

    /**
     * 处理POST注册请求
     *
     * 【处理流程】
     * 1. 设置响应格式为JSON，字符编码为UTF-8
     * 2. 读取并解析请求体中的JSON数据
     * 3. 提取用户注册信息（用户名、密码、昵称、电话、邮箱）
     * 4. 调用业务层userService.register()进行注册
     * 5. 返回注册成功/失败的JSON响应
     *
     * 【异常处理】
     * - JSON解析异常：返回500错误
     * - 数据库操作异常：返回500错误
     * - 用户名已存在：Service层返回false，正常响应
     *
     * @param req HTTP请求对象，包含注册信息
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

            // 【第三步】解析JSON请求体，提取注册信息
            JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();

            // 创建User对象，设置注册信息
            User user = new User();
            // 使用has()方法检查字段是否存在，避免抛出空指针异常
            // getAsString()将JSON值转换为String类型
            user.setUsername(jsonObject.has("username") ? jsonObject.get("username").getAsString() : null);
            user.setPassword(jsonObject.has("password") ? jsonObject.get("password").getAsString() : null);
            user.setNickname(jsonObject.has("nickname") ? jsonObject.get("nickname").getAsString() : null);
            user.setPhone(jsonObject.has("phone") ? jsonObject.get("phone").getAsString() : null);
            user.setEmail(jsonObject.has("email") ? jsonObject.get("email").getAsString() : null);

            // 【第四步】调用业务层执行注册
            // Service层会：
            // 1. 检查用户名是否已存在
            // 2. 对密码进行MD5加密
            // 3. 执行数据库插入操作
            // 4. 返回注册是否成功
            boolean success = userService.register(user);

            // 【第五步】返回注册结果的JSON响应
            // {"success": true} 表示注册成功
            // {"success": false} 表示注册失败（可能是用户名已存在）
            resp.getWriter().write("{\"success\":" + success + "}");

        } catch (Exception e) {
            // 【异常处理】
            // 打印异常堆栈信息到服务器日志，便于调试
            e.printStackTrace();

            // 返回500状态码（服务器内部错误）
            resp.setStatus(500);

            // 返回错误信息的JSON响应
            resp.getWriter().write("{\"success\":false,\"error\":\"服务器内部错误: " + e.getMessage() + "\"}");
        }
    }
}
