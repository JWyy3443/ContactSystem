<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>通讯录系统 - 注册</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/api.js"></script>
</head>
<body>
<div class="auth-container">
    <div class="auth-card">
        <div class="logo">
            <h1>📝 注册新账号</h1>
            <p>加入通讯录系统，管理您的人脉</p>
        </div>

        <div class="input-group">
            <label>用户名 *</label>
            <input type="text" id="username" placeholder="用户名">
        </div>

        <div class="input-group">
            <label>密码 *</label>
            <input type="password" id="password" placeholder="密码">
        </div>

        <div class="input-group">
            <label>昵称</label>
            <input type="text" id="nickname" placeholder="昵称">
        </div>

        <div class="input-group">
            <label>电话</label>
            <input type="tel" id="phone" placeholder="电话">
        </div>

        <div class="input-group">
            <label>邮箱</label>
            <input type="email" id="email" placeholder="邮箱">
        </div>

        <button class="btn-primary" id="registerBtn" onclick="handleRegister()">注册</button>

        <div class="auth-link">
            <a href="${pageContext.request.contextPath}/index.jsp">已有账号？去登录</a>
        </div>

        <div id="message" class="message"></div>
    </div>
</div>

<script>
    async function handleRegister() {
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const registerBtn = document.getElementById('registerBtn');

        if (!username || !password) {
            Utils.showMessage('message', '用户名和密码不能为空', 'error');
            return;
        }

        const user = {
            username: username,
            password: password,
            nickname: document.getElementById('nickname').value,
            phone: document.getElementById('phone').value,
            email: document.getElementById('email').value
        };

        Utils.setButtonLoading(registerBtn, true);

        try {
            const result = await API.register(user);
            if (result && result.success) {
                Utils.showMessage('message', '注册成功！即将跳转到登录页...', 'success');
                setTimeout(() => {
                    window.location.href = '${pageContext.request.contextPath}/index.jsp';
                }, 1500);
            } else {
                Utils.showMessage('message', '注册失败，用户名可能已存在', 'error');
            }
        } catch (error) {
            Utils.showMessage('message', '注册失败，请稍后重试', 'error');
        } finally {
            Utils.setButtonLoading(registerBtn, false);
        }
    }
</script>
</body>
</html>