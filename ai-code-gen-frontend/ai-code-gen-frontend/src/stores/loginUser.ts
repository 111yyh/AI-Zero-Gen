import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { getLoginUser } from '@/api/userController'

export const useLoginUserStore = defineStore('loginUser', () => {
    const loginUser = ref<API.LoginUserVO>({
        userName: '未登录',
    })
    
    const fetchLoginUser = async () => {
        const res = await getLoginUser();
        if (res.data?.code === 0 && res.data.data) {
            loginUser.value = res.data?.data
        }
    }

    const setLoginUser = (newLoginUser: API.LoginUserVO) => { 
        loginUser.value = newLoginUser
    }

  return { loginUser, fetchLoginUser, setLoginUser }
})
