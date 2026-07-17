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
 * 用户信息更新控制器
 *
 * 【功能说明】
 * - 更新用户的个人资料信息
 * - 包括：昵称、电话、邮箱
 * - 不允许通过此接口修改密码（安全性考虑）
 *
 * 【API接口】
 * - URL: /api/user/update
 * - 方法: PUT
 * - 请求体: {"id":1, "nickname":"xxx", "phone":"xxx", "email":"xxx"}
 * - 响应: {"success": true/false}
 *
 * 【更新限制】
 * - 只能更新昵称、电话、邮箱三个字段
 * - 用户ID用于指定要更新的用户
 * - 密码修改需要单独的接口（出于安全考虑）
 *
 * 【使用场景】
 * - 用户个人中心修改个人信息
 * - 管理员后台修改用户资料
 */
@WebServlet("/api/user/update")
public class UpdateUserServlet extends HttpServlet {

    /** 用户服务层实例，用于调用业务逻辑更新用户信息 */
    private UserService userService = new UserServiceImpl();

    /**
     * 处理PUT请求，更新用户信息
     *
     * 【处理流程】
     * 1. 设置响应格式为JSON，字符编码为UTF-8
     * 2. 读取并解析请求体中的JSON数据
     * 3. 提取用户ID和要更新的字段（昵称、电话、邮箱）
     * 4. 创建User对象，只设置允许更新的字段
     * 5. 调用业务层执行更新操作
     * 6. 返回更新成功/失败的JSON响应
     *
     * 【PUT vs POST】
     * - PUT用于更新完整资源，这里用于更新用户信息
     * - RESTful风格中，PUT通常用于替换整个资源
     *
     * @param req HTTP请求对象，包含更新信息
     * @param resp HTTP响应对象，用于返回JSON结果
     * @throws ServletException Servlet处理异常
     * @throws IOException IO读写异常
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 【第一步】设置响应格式：JSON类型，UTF-8字符编码
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // 【第二步】读取请求体数据
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = req.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        // 【第三步】解析JSON请求体
        JsonObject jsonObject = JsonParser.parseString(sb.toString()).getAsJsonObject();

        // 【第四步】提取更新数据，创建User对象
        // 注意：只提取允许更新的字段，不包括密码和用户名
        User user = new User();

        // 用户ID：指定要更新的用户
        // 使用三元表达式处理可能的空值，ID为0表示无效用户
        user.setId(jsonObject.has("id") ? jsonObject.get("id").getAsInt() : 0);

        // 昵称：用户的显示名称
        user.setNickname(jsonObject.has("nickname") ? jsonObject.get("nickname").getAsString() : null);

        // 电话：联系电话
        user.setPhone(jsonObject.has("phone") ? jsonObject.get("phone").getAsString() : null);

        // 邮箱：电子邮箱
        user.setEmail(jsonObject.has("email") ? jsonObject.get("email").getAsString() : null);

        // 【第五步】调用业务层执行更新
        // updateUserInfo方法会：
        // 1. 根据用户ID查找用户
        // 2. 更新用户的昵称、电话、邮箱字段
        // 3. 返回更新是否成功
        boolean success = userService.updateUserInfo(user);

        // 【第六步】返回更新结果的JSON响应
        resp.getWriter().write("{\"success\":" + success + "}");
    }
}
