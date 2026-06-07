<template>
  <router-view />
</template>

<script setup>
</script>

<style>
/* Import Space Grotesk and JetBrains Mono fonts */
@import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@300;400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap');

/* Iconify Solar icons */
@import url('https://code.iconify.design/3/3.1.0/iconify.min.js');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

:root {
  --bg-primary: #FFFFFF;
  --bg-secondary: #F8FAFC;
  --bg-tertiary: #F1F5F9;
  --bg-card: #FFFFFF;
  --text-primary: #1E293B;
  --text-secondary: #64748B;
  --text-muted: #94A3B8;
  --accent-mint: #10B981;
  --accent-amber: #F59E0B;
  --accent-coral: #EF4444;
  --accent-sky: #0EA5E9;
  --border: #E2E8F0;
}

body {
  font-family: 'Space Grotesk', sans-serif;
  background: var(--bg-primary);
  color: var(--text-primary);
  min-height: 100vh;
  position: relative;
  overflow-x: hidden;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

#app {
  width: 100%;
  height: 100vh;
}

/* Button base styles */
.btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: none;
  font-family: inherit;
}

.btn-secondary {
  background: var(--bg-tertiary);
  color: var(--text-primary);
  border: 1px solid var(--border);
}

.btn-secondary:hover {
  background: var(--bg-secondary);
  border-color: var(--text-muted);
}

.btn-primary {
  background: linear-gradient(135deg, var(--accent-mint) 0%, #059669 100%);
  color: var(--bg-primary);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(16, 185, 129, 0.3);
}

/* Input base styles */
.input {
  width: 100%;
  padding: 14px 20px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 12px;
  font-size: 14px;
  color: var(--text-primary);
  font-family: inherit;
  transition: all 0.3s;
}

.input::placeholder {
  color: var(--text-muted);
}

.input:focus {
  outline: none;
  border-color: var(--accent-sky);
  box-shadow: 0 0 0 3px rgba(14, 165, 233, 0.15);
}

/* Card base styles */
.card {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 1px 3px 0 rgb(0 0 0 / 5%), 0 1px 2px -1px rgb(0 0 0 / 5%);
}

/* Panel header styles */
.panel-header {
  padding: 20px 24px;
  border-bottom: 1px solid var(--border);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.panel-content {
  padding: 24px;
}

/* Tag styles */
.tag {
  padding: 4px 10px;
  background: var(--bg-tertiary);
  border-radius: 6px;
  font-size: 12px;
  color: var(--text-muted);
}

/* Status tags */
.status-tag {
  font-size: 12px;
  font-weight: 500;
  padding: 4px 12px;
  border-radius: 20px;
}

.status-success {
  background: rgba(16, 185, 129, 0.1);
  color: #059669;
}

.status-warning {
  background: rgba(245, 158, 11, 0.1);
  color: #D97706;
}

.status-error {
  background: rgba(239, 68, 68, 0.1);
  color: #DC2626;
}

.status-info {
  background: rgba(14, 165, 233, 0.1);
  color: #0284C7;
}

/* Monospace numbers */
.mono {
  font-family: 'JetBrains Mono', monospace;
}

/* Loading animation */
.loading {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Fade in animation */
.fade-in {
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Scrollbar styling for light theme */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: var(--bg-secondary);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #CBD5E1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #94A3B8;
}
</style>