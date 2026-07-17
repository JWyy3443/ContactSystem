<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>通讯录系统 - 登录</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/api.js"></script>
    <script src="${pageContext.request.contextPath}/js/Utils.js"></script>
</head>
<body>
<div class="auth-container">
    <div class="auth-card">
        <div class="logo">
            <h1>📒 通讯录系统</h1>
            <p>欢迎回来，请登录您的账号</p>
        </div>

        <div class="input-group">
            <label>用户名</label>
            <input type="text" id="username" placeholder="请输入用户名">
        </div>

        <div class="input-group">
            <label>密码</label>
            <input type="password" id="password" placeholder="请输入密码">
        </div>

        <button class="btn-primary" id="loginBtn" onclick="handleLogin()">登录</button>

        <div class="auth-link">
            <a href="${pageContext.request.contextPath}/register.jsp">还没有账号？立即注册</a>
        </div>

        <div id="message" class="message"></div>
    </div>
</div>

<script>
    async function handleLogin() {
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const loginBtn = document.getElementById('loginBtn');

        if (!username || !password) {
            Utils.showMessage('message', '请输入用户名和密码', 'error');
            return;
        }

        Utils.setButtonLoading(loginBtn, true);

        try {
            const user = await API.login(username, password);
            if (user) {
                localStorage.setItem('user', JSON.stringify(user));
                Utils.showMessage('message', '登录成功！正在跳转...', 'success');
                setTimeout(() => {
                    window.location.href = 'dashboard.jsp';
                }, 1000);
            } else {
                Utils.showMessage('message', '用户名或密码错误', 'error');
            }
        } catch (error) {
            Utils.showMessage('message', '登录失败，请稍后重试', 'error');
        } finally {
            Utils.setButtonLoading(loginBtn, false);
        }
    }
</script>
</body>
</html>