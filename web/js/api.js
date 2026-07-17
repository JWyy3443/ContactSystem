/**
 * API ����ģ��
 * ��װ�������˵�ͨ��
 */

const API = {
    // ����·�����Զ���ȡ��ǰ��Ŀ·����
    baseUrl: window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)) || '',

    /**
     * ���� AJAX ����
     */
    async request(url, options = {}) {
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json'
            }
        };

        const config = { ...defaultOptions, ...options };

        try {
            const response = await fetch(this.baseUrl + url, config);

            if (response.status === 401) {
                window.location.href = this.baseUrl + '/index.jsp';
                return null;
            }

            const data = await response.json();
            return data;
        } catch (error) {
            console.error('����ʧ��:', error);
            throw error;
        }
    },

    /**
     * ��¼
     */
    login(username, password) {
        return this.request('/api/login', {
            method: 'POST',
            body: JSON.stringify({username, password})
        });
    },

    /**
     * ע��
     */
    register(user) {
        return this.request('/api/register', {
            method: 'POST',
            body: JSON.stringify(user)
        });
    },

    /**
     * ��ȡ�û���Ϣ
     */
    getUserInfo(userId) {
        return this.request(`/api/user/${userId}`);
    },

    /**
     * �����û���Ϣ
     */
    updateUser(user) {
        return this.request('/api/user/update', {
            method: 'PUT',
            body: JSON.stringify(user)
        }).then(res => ({ success: true, ...res }));
    },

    /**
     * ��ȡ�����û�������Ա��
     */
    getAllUsers() {
        return this.request('/api/admin/users');
    },

    /**
     * �����û�������Ա��
     */
    searchUsers(keyword) {
        return this.request(`/api/admin/search?keyword=${encodeURIComponent(keyword)}`);
    },

    /**
     * ��ȡͳ�����ݣ�����Ա��
     */
    getStatistics() {
        return this.request('/api/admin/statistics');
    }
};

/**
 * ͨ�ù��ߺ���
 */
const Utils = {
    /**
     * ��ʾ��ʾ��Ϣ
     */
    showMessage(elementId, message, type = 'success') {
        const msgDiv = document.getElementById(elementId);
        if (msgDiv) {
            msgDiv.textContent = message;
            msgDiv.className = `message ${type}`;
            msgDiv.style.display = 'block';

            setTimeout(() => {
                msgDiv.style.display = 'none';
            }, 3000);
        }
    },

    /**
     * ���ð�ť����״̬
     */
    setButtonLoading(button, loading) {
        if (loading) {
            button.disabled = true;
            button.classList.add('btn-loading');
            const originalText = button.textContent;
            button.setAttribute('data-original-text', originalText);
            button.innerHTML = '<span class="loading-spinner"></span> ������...';
        } else {
            button.disabled = false;
            button.classList.remove('btn-loading');
            const originalText = button.getAttribute('data-original-text');
            if (originalText) {
                button.textContent = originalText;
            }
        }
    },

    /**
     * ��ȡ URL ����
     */
    getUrlParam(name) {
        const params = new URLSearchParams(window.location.search);
        return params.get(name);
    }
};

/**
 * ҳ���л�
 */
function initPageSwitch() {
    const navItems = document.querySelectorAll('.nav-item');
    const pages = document.querySelectorAll('.page');

    navItems.forEach(item => {
        item.addEventListener('click', () => {
            const pageId = item.dataset.page;

            navItems.forEach(nav => nav.classList.remove('active'));
            item.classList.add('active');

            pages.forEach(page => page.classList.remove('active'));
            const targetPage = document.getElementById(`${pageId}Page`);
            if (targetPage) targetPage.classList.add('active');

            const pageTitle = document.getElementById('pageTitle');
            if (pageTitle) {
                const titleMap = {
                    profile: '������Ϣ',
                    users: '�û�����',
                    stats: 'ͳ�Ʒ���'
                };
                pageTitle.textContent = titleMap[pageId] || 'ͨѶ¼ϵͳ';
            }

            // ����ҳ����ػص�
            if (window[`on${pageId}PageShow`]) {
                window[`on${pageId}PageShow`]();
            }
        });
    });
}

/**
 * �˳���¼
 */
function logout() {
    // 清理客户端存储的用户信息
    localStorage.removeItem('user');
    sessionStorage.removeItem('user');
    // 重定向到退出接口
    window.location.href = API.baseUrl + '/logout';
}