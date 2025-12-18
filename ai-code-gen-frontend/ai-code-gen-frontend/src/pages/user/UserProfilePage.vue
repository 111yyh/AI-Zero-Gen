<template>
    <div id="userProfilePage">
        <h2 class="title">个人中心</h2>
        <a-card>
            <div class="profile-area">
                <div class="avatar">
                    <a-image :src="user.userAvatar" width="120" />
                </div>

                <div class="info">
                    <template v-if="!editMode">
                        <p><strong>账号：</strong>{{ user.userAccount }}</p>
                        <p><strong>用户名：</strong>{{ user.userName }}</p>
                        <p><strong>简介：</strong>{{ user.userProfile }}</p>
                        <a-button type="primary" @click="edit">编辑</a-button>
                    </template>

                    <template v-else>
                        <a-form :model="user" @finish="save">
                            <a-form-item label="用户名">
                                <a-input v-model:value="user.userName" />
                            </a-form-item>
                            <a-form-item label="头像 URL">
                                <a-input v-model:value="user.userAvatar" />
                            </a-form-item>
                            <a-form-item label="简介">
                                <a-textarea v-model:value="user.userProfile" rows="4" />
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" html-type="submit">保存</a-button>
                                <a-button style="margin-left:8px" @click="cancel">取消</a-button>
                            </a-form-item>
                        </a-form>
                    </template>
                </div>
            </div>
        </a-card>
    </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser'
import { update } from '@/api/userController'

const loginUserStore = useLoginUserStore()
const editMode = ref(false)

const user = reactive({
    id: undefined,
    userAccount: '',
    userName: '',
    userAvatar: '',
    userProfile: ''
} as API.LoginUserVO)

const loadUser = async () => {
    // 确保已加载当前登录用户
    if (!loginUserStore.loginUser || !loginUserStore.loginUser.id) {
        await loginUserStore.fetchLoginUser()
    }
    Object.assign(user, loginUserStore.loginUser)
}

onMounted(loadUser)

const edit = () => {
    editMode.value = true
}

const save = async () => {
    const res = await update({
        id: user.id,
        userName: user.userName,
        userAvatar: user.userAvatar,
        userProfile: user.userProfile
    })
    if (res.data?.code === 0) {
        message.success('保存成功')
        loginUserStore.setLoginUser(user);
        Object.assign(user, loginUserStore.loginUser)
        editMode.value = false
    } else {
        message.error('保存失败，' + res.data?.message)
    }
}

const cancel = () => {
    Object.assign(user, loginUserStore.loginUser)
    editMode.value = false
}
</script>

<style scoped>
.title {
    margin-bottom: 16px;
}

.profile-area {
    display: flex;
    gap: 24px;
    align-items: flex-start;
}

.avatar {
    flex: 0 0 120px;
}

.info {
    flex: 1;
}
</style>