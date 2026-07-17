<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.contacts.entity.User" %>
<%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
    boolean isAdmin = "admin".equals(currentUser.getRole());
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>通讯录管理系统 - 工作台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/api.js"></script>
</head>
<body>
<div class="sidebar">
    <div class="sidebar-header">
        <h2>📒 通讯录</h2>
        <p>欢迎 <%= currentUser.getNickname() %></p>
    </div>
    <ul class="nav-menu">
        <li class="nav-item active" data-page="profile">
            <span class="nav-icon">👤</span> 个人信息
        </li>
        <% if (isAdmin) { %>
        <li class="nav-item" data-page="users">
            <span class="nav-icon">👥</span> 用户管理
        </li>
        <li class="nav-item" data-page="stats">
            <span class="nav-icon">📊</span> 统计分析
        </li>
        <% } %>
    </ul>
</div>

<div class="main-content">
    <div class="top-bar">
        <h3 id="pageTitle">个人信息</h3>
        <div class="user-info">
            <span class="user-name"><%= currentUser.getNickname() %></span>
            <button class="logout-btn" onclick="logout()">退出登录</button>
        </div>
    </div>

    <!-- 个人信息页面 -->
    <div id="profilePage" class="page active">
        <div class="card">
            <div class="card-title">修改个人信息</div>
            <div class="form-row">
                <div class="form-group">
                    <label>昵称</label>
                    <input type="text" id="nickname" value="<%= currentUser.getNickname() != null ? currentUser.getNickname() : "" %>">
                </div>
                <div class="form-group">
                    <label>电话</label>
                    <input type="tel" id="phone" value="<%= currentUser.getPhone() != null ? currentUser.getPhone() : "" %>">
                </div>
                <div class="form-group">
                    <label>邮箱</label>
                    <input type="email" id="email" value="<%= currentUser.getEmail() != null ? currentUser.getEmail() : "" %>">
                </div>
            </div>
            <button class="btn-primary" id="updateBtn" onclick="updateProfile()">保存修改</button>
        </div>
    </div>

    <!-- 用户管理页面（仅管理员） -->
    <div id="usersPage" class="page">
        <div class="card">
            <div class="card-title">用户列表</div>
            <div class="search-bar">
                <input type="text" id="searchKeyword" placeholder="搜索用户名或昵称...">
                <button class="btn-primary" onclick="searchUsers()">搜索</button>
            </div>
            <div class="table-wrapper">
                <table class="data-table">
                    <thead>
                    <tr><th>ID</th><th>用户名</th><th>昵称</th><th>电话</th><th>邮箱</th><th>角色</th></tr>
                    </thead>
                    <tbody id="userTableBody">
                    <tr><td colspan="6" style="text-align:center;">加载中...</td></tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- 统计分析页面（仅管理员） -->
    <div id="statsPage" class="page">
        <div class="stats-grid" id="statsGrid">
            <div class="stat-card"><div class="stat-number">-</div><div class="stat-label">总用户数</div></div>
            <div class="stat-card"><div class="stat-number">-</div><div class="stat-label">管理员</div></div>
            <div class="stat-card"><div class="stat-number">-</div><div class="stat-label">普通用户</div></div>
        </div>
    </div>
</div>

<script>
    const currentUserId = <%= currentUser.getId() %>;
    const isAdmin = <%= isAdmin %>;

    // 更新个人信息
    window.updateProfile = async function() {
        const updateBtn = document.getElementById('updateBtn');
        const user = {
            id: currentUserId,
            nickname: document.getElementById('nickname').value,
            phone: document.getElementById('phone').value,
            email: document.getElementById('email').value
        };

        Utils.setButtonLoading(updateBtn, true);

        try {
            const result = await API.updateUser(user);
            if (result && result.success) {
                alert('修改成功！');
            } else {
                alert('修改失败，请稍后重试');
            }
        } catch (error) {
            alert('修改失败：' + (error.message || '网络错误'));
        } finally {
            Utils.setButtonLoading(updateBtn, false);
        }
    };

    // 加载用户列表
    window.loadUsers = async function() {
        try {
            const users = await API.getAllUsers();
            const tbody = document.getElementById('userTableBody');
            if (users && users.length > 0) {
                let html = '';
                users.forEach(u => {
                    html += '<tr>' +
                            '<td>' + u.id + '</td>' +
                            '<td>' + escapeHtml(u.username) + '</td>' +
                            '<td>' + escapeHtml(u.nickname || '-') + '</td>' +
                            '<td>' + escapeHtml(u.phone || '-') + '</td>' +
                            '<td>' + escapeHtml(u.email || '-') + '</td>' +
                            '<td>' + (u.role === 'admin' ? '管理员' : '普通用户') + '</td>' +
                        '</tr>';
                });
                tbody.innerHTML = html;
            } else {
                tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;">暂无数据</td></tr>';
            }
        } catch (error) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;">加载失败</td></tr>';
        }
    };

    // 搜索用户
    window.searchUsers = async function() {
        const keyword = document.getElementById('searchKeyword').value;
        if (!keyword.trim()) {
            loadUsers();
            return;
        }

        try {
            const users = await API.searchUsers(keyword);
            const tbody = document.getElementById('userTableBody');
            if (users && users.length > 0) {
                let html = '';
                users.forEach(u => {
                    html += '<tr>' +
                            '<td>' + u.id + '</td>' +
                            '<td>' + escapeHtml(u.username) + '</td>' +
                            '<td>' + escapeHtml(u.nickname || '-') + '</td>' +
                            '<td>' + escapeHtml(u.phone || '-') + '</td>' +
                            '<td>' + escapeHtml(u.email || '-') + '</td>' +
                            '<td>' + (u.role === 'admin' ? '管理员' : '普通用户') + '</td>' +
                        '</tr>';
                });
                tbody.innerHTML = html;
            } else {
                tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;">未找到匹配的用户</td></tr>';
            }
        } catch (error) {
            alert('搜索失败');
        }
    };

    // 加载统计数据
    window.loadStatistics = async function() {
        try {
            const stats = await API.getStatistics();
            if (stats) {
                const cards = document.querySelectorAll('#statsGrid .stat-card');
                if (cards.length >= 3) {
                    cards[0].querySelector('.stat-number').textContent = stats.totalUsers || 0;
                    cards[1].querySelector('.stat-number').textContent = stats.adminCount || 0;
                    cards[2].querySelector('.stat-number').textContent = stats.userCount || 0;
                }
            }
        } catch (error) {
            console.error('加载统计失败:', error);
        }
    };

    // 页面切换回调
    window.onusersPageShow = loadUsers;
    window.onstatsPageShow = loadStatistics;

    // HTML转义
    function escapeHtml(str) {
        if (!str) return '';
        return str.replace(/[&<>]/g, function(m) {
            if (m === '&') return '&amp;';
            if (m === '<') return '&lt;';
            if (m === '>') return '&gt;';
            return m;
        });
    }

    // 初始化页面切换
    initPageSwitch();
</script>
</body>
</html>