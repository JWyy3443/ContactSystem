package com.contacts.servlet;

import com.contacts.entity.User;
import com.contacts.service.UserService;
import com.contacts.service.UserServiceImpl;
import com.contacts.util.GsonUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户信息查询控制器
 *
 * 【功能说明】
 * - 根据用户ID查询用户详细信息
 * - 返回用户的完整信息（密码除外）
 * - 用于个人中心、用户详情展示等场景
 *
 * 【API接口】
 * - URL: /api/user/{id}  （如 /api/user/1）
 * - 方法: GET
 * - 响应: {"id":1,"username":"xxx","nickname":"xxx","phone":"xxx","email":"xxx","role":"xxx"}
 *
 * 【路径参数】
 * - {id}: 用户的数字ID，从URL路径中提取
 * - 例如 /api/user/123 表示查询ID为123的用户
 *
 * 【安全说明】
 * - 返回的User对象中密码字段被设置为null，不返回给客户端
 * - 建议添加权限验证：只能查询自己或管理员可以查询所有用户
 */
@WebServlet("/api/user/*")
public class UserInfoServlet extends HttpServlet {

    /** 用户服务层实例，用于调用业务逻辑查询用户信息 */
    private UserService userService = new UserServiceImpl();

    /** Gson实例，用于将User对象序列化为JSON字符串 */
    private Gson gson = GsonUtil.getInstance();

    /**
     * 处理GET请求，根据用户ID查询用户信息
     *
     * 【处理流程】
     * 1. 设置响应格式为JSON，字符编码为UTF-8
     * 2. 从URL路径中提取用户ID（如 /api/user/123 中的 123）
     * 3. 调用业务层根据ID查询用户信息
     * 4. 如果用户存在，设置密码为null后返回JSON
     * 5. 如果用户不存在，返回404错误
     *
     * 【URL路径解析】
     * - req.getPathInfo() 返回 /123（包含前导斜杠）
     * - substring(1) 去掉前导斜杠得到 "123"
     * - Integer.parseInt() 将字符串转换为整数
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

        // 【第二步】从URL路径中提取用户ID
        // req.getPathInfo() 返回路径中Servlet部分之后的内容
        // 例如：URL为 /api/user/123 时，pathInfo 为 /123
        String pathInfo = req.getPathInfo();

        // 去掉前导斜杠，将 "/123" 转换为 "123"
        // 然后解析为整数类型
        int userId = Integer.parseInt(pathInfo.substring(1));

        // 【第三步】调用业务层查询用户信息
        // findById方法会根据用户ID从数据库查询完整用户信息
        User user = userService.findById(userId);

        // 【第四步】处理查询结果
        if (user != null) {
            // 用户存在：清除密码后返回用户信息
            // 密码不能返回给客户端，这是基本的安全措施
            user.setPassword(null);

            // 使用Gson将User对象序列化为JSON字符串并写入响应
            resp.getWriter().write(gson.toJson(user));
        } else {
            // 用户不存在：返回404状态码和错误信息
            resp.setStatus(404);
            resp.getWriter().write("{\"error\":\"User not found\"}");
        }
    }
}
