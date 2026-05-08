<template>
  <router-view />
</template>

<script setup>
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background: #ffffff;
  color: #0f172a;
  min-height: 100vh;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* 全局滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

/* Element Plus 组件样式覆盖 */
.el-button {
  border-radius: 10px;
  font-weight: 500;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.el-button--primary {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  border: none;
}

.el-button--primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.el-input__wrapper {
  border-radius: 10px;
  box-shadow: 0 0 0 1px #e2e8f0 inset;
}

.el-input__wrapper:hover {
  box-shadow: 0 0 0 1px #cbd5e1 inset;
}

.el-input__wrapper.is-focus {
  box-shadow: 0 0 0 1px #3b82f6 inset, 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.el-card {
  border-radius: 16px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1);
}

.el-card__header {
  border-bottom: 1px solid #f1f5f9;
  padding: 20px 24px;
}

.el-table {
  --el-table-header-bg-color: #f8fafc;
  --el-table-border-color: #f1f5f9;
  --el-table-row-hover-bg-color: #f8fafc;
}

.el-table th.el-table__cell {
  font-weight: 600;
  color: #475569;
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.el-tag {
  border-radius: 6px;
  font-weight: 500;
}

.el-dialog {
  border-radius: 16px;
  overflow: hidden;
}

.el-dialog__header {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  padding: 20px 24px;
  border-bottom: 1px solid #e2e8f0;
}

.el-dialog__title {
  font-weight: 600;
  color: #0f172a;
}

.el-pagination {
  --el-pagination-button-bg-color: #ffffff;
  --el-pagination-hover-color: #3b82f6;
}

.el-pagination .el-pager li {
  border-radius: 8px;
  font-weight: 500;
}

.el-pagination .el-pager li.is-active {
  background: #3b82f6;
}

.el-form-item__label {
  font-weight: 500;
  color: #475569;
}

.el-select .el-input__wrapper {
  border-radius: 10px;
}

.el-dropdown-menu {
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
}

.el-dropdown-menu__item {
  border-radius: 8px;
  margin: 2px 6px;
  padding: 10px 16px;
}

.el-dropdown-menu__item:hover {
  background: #f8fafc;
}

.el-message-box {
  border-radius: 16px;
}

.el-message {
  border-radius: 10px;
}
</style>
