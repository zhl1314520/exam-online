<template>
  <div class="test-page">
    <h1>测试页面</h1>
    <p>如果您能看到这段文字，说明Vue渲染正常。</p>
    <button @click="count++">点击次数: {{ count }}</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const count = ref(0)
</script>

<style>
.test-page {
  padding: 20px;
  background: #ff0000 !important;
  min-height: 100vh;
}
h1 {
  color: #ffffff !important;
  font-size: 24px;
}
p {
  color: #ffffff !important;
  font-size: 16px;
}
button {
  padding: 10px 20px;
  font-size: 16px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background: #0056b3;
}
</style>