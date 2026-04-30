import { ACCESS_ENUM } from '@/enum/accessEnum'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../pages/HomePage.vue'),
      meta: {
        access: ACCESS_ENUM.NOT_LOGIN,
        menu: {
          key: '/',
          label: '首页',
          order: 1,
        }
      }
    },
    {
      path: '/noauth',
      name: 'noauth',
      component: () => import('../pages/NoAuth.vue'),
      meta: {
        access: ACCESS_ENUM.NOT_LOGIN
      }
    },
    {
      path: '/user/login',
      name: 'login',
      component: () => import('../pages/user/UserLoginPage.vue'),
      meta: {
        access: ACCESS_ENUM.NOT_LOGIN
      }
    },
    {
      path: '/user/register',
      name: 'register',
      component: () => import('../pages/user/UserRegisterPage.vue'),
      meta: {
        access: ACCESS_ENUM.NOT_LOGIN
      }
    },
    {
      path: '/user/profile',
      name: 'profile',
      component: () => import('../pages/user/UserProfilePage.vue'),
      meta: {
        access: ACCESS_ENUM.USER
      }
    },
    {
      path: '/admin/userManage',
      name: 'userManage',
      component: () => import('../pages/admin/UserManagePage.vue'),
      meta: {
        access: ACCESS_ENUM.ADMIN,
        menu: {
          key: '/admin/userManage',
          label: '用户管理',
          order: 2
        }
      }
    },
  ],
})

export default router