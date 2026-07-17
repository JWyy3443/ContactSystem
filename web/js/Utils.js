/**
 * 工具函数
 */
const Utils = {
    /**
     * 显示消息
     * @param {string} elementId - 消息容器ID
     * @param {string} message - 消息内容
     * @param {string} type - 消息类型: success, error, info
     */
    showMessage(elementId, message, type = 'info') {
        const element = document.getElementById(elementId);
        if (element) {
            element.textContent = message;
            element.className = 'message ' + type;
            element.style.display = 'block';
            setTimeout(() => {
                element.style.display = 'none';
            }, 3000);
        }
    },

    /**
     * 设置按钮加载状态
     * @param {HTMLElement} button - 按钮元素
     * @param {boolean} loading - 是否加载中
     */
    setButtonLoading(button, loading) {
        if (button) {
            if (loading) {
                button.disabled = true;
                button.innerHTML = '<span class="loading"></span> 登录中...';
            } else {
                button.disabled = false;
                button.innerHTML = '登录';
            }
        }
    }
};
