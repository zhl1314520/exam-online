<template>
  <div class="layout-container">
    <Sidebar :collapsed="collapsed" @toggle="toggleSidebar" />
    <div class="main-container" :class="{ 'sidebar-collapsed': collapsed }">
      <Header @toggle="toggleSidebar" :collapsed="collapsed" />
      <main class="content-area">
        <div class="content-wrapper fade-in">
          <router-view />
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Sidebar from './Sidebar.vue'
import Header from './Header.vue'

const collapsed = ref(false)

const toggleSidebar = () => {
  collapsed.value = !collapsed.value
}
</script>

<style lang="scss" scoped>
@use '@/styles/variables' as *;

.layout-container {
  display: flex;
  width: 100%;
  height: 100vh;
  position: relative;
  background: $bg-primary;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin-left: 240px;
  transition: margin-left $transition-normal $ease-minimal;
  background: $bg-primary;

  &.sidebar-collapsed {
    margin-left: 72px;
  }
}

.content-area {
  flex: 1;
  padding: $spacing-xl;
  overflow-y: auto;
  overflow-x: hidden;
  position: relative;
  background: $bg-secondary;
}

.content-wrapper {
  max-width: 1280px;
  margin: 0 auto;
  width: 100%;
}
</style>