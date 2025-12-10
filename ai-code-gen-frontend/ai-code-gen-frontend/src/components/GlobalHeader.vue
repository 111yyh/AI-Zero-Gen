<template>
  <a-layout-header class="global-header">
    <div class="header-content">
      <!-- 左侧：Logo 和标题 -->
      <div class="header-left">
        <img alt="Logo" class="logo" src="@/assets/logo.png" />
        <span class="site-title">AI 零代码应用生成平台</span>
      </div>

      <!-- 中间：菜单 -->
      <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="horizontal"
        :items="menuItems"
        class="header-menu"
        @click="handleMenuClick"
      />

      <!-- 右侧：用户信息 -->
      <div class="header-right">
        <a-button type="primary" @click="handleLogin">
          登录
        </a-button>
      </div>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import type { MenuProps } from 'ant-design-vue'

const router = useRouter()
const route = useRoute()

// 菜单配置
const menuItems = ref<MenuProps['items']>([
  {
    key: '/',
    label: '首页',
  },
  {
    key: '/about',
    label: '关于',
  },
])

// 当前选中的菜单项
const selectedKeys = ref<string[]>([])

// 监听路由变化，更新选中的菜单项
router.afterEach((to) => { 
    selectedKeys.value = [to.path]
})

// 菜单点击事件
const handleMenuClick: MenuProps['onClick'] = (e) => {
  router.push(e.key as string)
}

// 登录按钮点击事件
const handleLogin = () => {
  // TODO: 实现登录逻辑
  console.log('登录')
}
</script>

<style scoped lang="css">
.global-header {
  background: #fff;
  padding: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.logo {
  height: 40px;
  width: auto;
}

.site-title {
  font-size: 18px;
  font-weight: 600;
  color: #1890ff;
  white-space: nowrap;
}

.header-menu {
  flex: 1;
  min-width: 0;
  justify-content: center;
  border-bottom: none;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    padding: 0 16px;
  }

  .site-title {
    font-size: 16px;
  }

  .header-menu {
    display: none;
  }
}

@media (max-width: 480px) {
  .site-title {
    display: none;
  }

  .logo {
    height: 32px;
  }
}
</style>

