<template>
  <div class="layout-container">
    <Sidebar />
    <div class="main-wrapper">
      <Header />
      <main class="main-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import Sidebar from './Sidebar.vue'
import Header from './Header.vue'
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.layout-container {
  display: flex;
  min-height: 100vh;
  background: $bg-secondary;
  position: relative;
  z-index: 1;
}

.main-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin-left: 280px;
  min-height: 100vh;
  position: relative;
  z-index: 1;
}

.main-content {
  flex: 1;
  padding: 0 40px 40px;
  overflow: auto;
  position: relative;
  z-index: 1;
}
</style>