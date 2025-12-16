<template>
    <a-table :columns="columns" :data-source="data">
        <template #headerCell="{ column }">
            <template v-if="column.key === 'name'">
                <span>
                    <smile-outlined />
                    Name
                </span>
            </template>
        </template>

        <template #bodyCell="{ column, record }">
            <template v-if="column.dataIndex === 'userAvatar'">
                <a-image :src="record.userAvatar" :width="120" />
            </template>
            <template v-else-if="column.dataIndex === 'userRole'">
                <div v-if="record.userRole === 'admin'">
                    <a-tag color="green">管理员</a-tag>
                </div>
                <div v-else>
                    <a-tag color="blue">普通用户</a-tag>
                </div>
            </template>
            <template v-else-if="column.dataIndex === 'createTime'">
                {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
            </template>
            <template v-else-if="column.key === 'action'">
                <a-button danger>删除</a-button>
            </template>
        </template>

    </a-table>
</template>
<script lang="ts" setup>
import { listUserVoByPage } from '@/api/userController';
import { SmileOutlined, DownOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import { onMounted, reactive, ref } from 'vue';
import dayjs from "dayjs";

const columns = [
    {
        title: 'id',
        dataIndex: 'id',
    },
    {
        title: '账号',
        dataIndex: 'userAccount',
    },
    {
        title: '用户名',
        dataIndex: 'userName',
    },
    {
        title: '头像',
        dataIndex: 'userAvatar',
    },
    {
        title: '简介',
        dataIndex: 'userProfile',
    },
    {
        title: '用户角色',
        dataIndex: 'userRole',
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
    },
    {
        title: '操作',
        key: 'action',
    },
]

const data = ref<API.UserVO[]>()
const total = ref(0)

const searchParam = reactive<API.UserQueryRequest>({
    pageNum: 1,
    pageSize: 10
})

const fetchFormData = async () => {
    const res = await listUserVoByPage(searchParam)
    if (res.data?.code === 0 && res.data?.data) {
        data.value = res.data?.data?.records ?? []
        total.value = res.data?.data?.totalRow ?? 0
    } else {
        message.error("获取数据失败," + res?.data?.message)
    }
}

onMounted(() => {
    fetchFormData()
})

</script>
