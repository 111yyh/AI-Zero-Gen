<template>
  <a-layout-footer class="global-footer">
    <div class="footer-content">
      <p>AI 零代码应用生成平台</p>
    </div>
  </a-layout-footer>
</template>

<script setup lang="ts">
// Footer 组件无需逻辑
</script>

<style scoped lang="css">
.global-footer {
  background: #f0f2f5;
  text-align: center;
  padding: 24px 0;
  margin-top: auto;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.footer-content p {
  margin: 0;
  color: rgba(0, 0, 0, 0.65);
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .global-footer {
    padding: 16px 0;
  }

  .footer-content {
    padding: 0 16px;
  }

  .footer-content p {
    font-size: 12px;
  }
}
</style>

