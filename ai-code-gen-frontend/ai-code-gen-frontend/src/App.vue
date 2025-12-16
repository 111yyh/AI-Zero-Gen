<script setup lang="ts">
import { ref } from 'vue'
import BasicLayout from './layouts/BasicLayout.vue'
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import { useLoginUserStore } from './stores/loginUser';

const locale = ref(zhCN)

const loginUserStore = useLoginUserStore();
loginUserStore.fetchLoginUser();
</script>

<template>
    <!-- <a-config-provider :locale="locale"> -->
        <BasicLayout />
    <!-- </a-config-provider> -->
</template>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial,
    'Noto Sans', sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol',
    'Noto Color Emoji';
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

#app {
  width: 100%;
  min-height: 100vh;
}
</style>
