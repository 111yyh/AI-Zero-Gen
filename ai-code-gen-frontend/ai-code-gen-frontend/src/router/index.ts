import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../pages/HomePage.vue'),
    },
    {
      path: '/user/login',
      name: 'login',
      component: () => import('../pages/user/UserLoginPage.vue'),
    },
    {
      path: '/user/register',
      name: 'register',
      component: () => import('../pages/user/UserRegisterPage.vue'),
    },
    {
      path: '/admin/userManage',
      name: 'userManage',
      component: () => import('../pages/admin/UserManagePage.vue'),
    },
  ],
})

export default router
