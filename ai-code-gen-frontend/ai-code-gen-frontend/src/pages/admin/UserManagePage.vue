<template>
    <a-form name="customized_form_controls" layout="inline" :model="searchParams" @finish="doSearch">
        <a-form-item name="账号" label="账号">
            <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" />
        </a-form-item>
        <a-form-item name="用户名" label="用户名">
            <a-input v-model:value="searchParams.userName" placeholder="输入用户名" />
        </a-form-item>
        <a-form-item>
            <a-button type="primary" html-type="submit">搜索</a-button>
        </a-form-item>
    </a-form>
    <a-divider />
    <a-table :columns="columns" :data-source="data" :pagination="pagination" @change="doTableChange">
        <template #headerCell="{ column }">
            <template v-if="column.key === 'name'">
                <span>
                    <smile-outlined />
                    Name
                </span>
            </template>
        </template>

        <template #bodyCell="{ column, text, record }">
            <template v-if="['userAccount', 'userName', 'userProfile'].includes(column.dataIndex)">
                <div>
                    <a-input v-if="editableData[record.id]"
                        v-model:value="editableData[record.id][column.dataIndex as keyof API.UserVO]"
                        style="margin: -5px 0" />
                    <template v-else>
                        {{ text }}
                    </template>
                </div>
            </template>
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
                <div class="editable-row-operations">
                    <span v-if="editableData[record.id]">
                        <a-typography-link @click="save(record.id)">保存</a-typography-link>
                        <a-typography-link @click="cancel(record.id)">取消</a-typography-link>
                    </span>
                    <span v-else>
                        <a @click="edit(record.id)">编辑</a>
                    </span>
                    <span v-show="record.userRole !== 'admin'">
                        <a @click="doDelete(record.id)">删除</a>
                    </span>
                </div>
            </template>
        </template>

    </a-table>
</template>
<script lang="ts" setup>
import { deleteUser, listUserVoByPage, update } from '@/api/userController';
import { SmileOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import { computed, onMounted, reactive, ref, type UnwrapRef } from 'vue';
import dayjs from "dayjs";
import { cloneDeep } from 'lodash';

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
const data = ref<API.UserVO[]>([])
const total = ref(0)

const searchParams = reactive<API.UserQueryRequest>({
    pageNum: 1,
    pageSize: 4
})

// 分页参数
const pagination = computed(() => {
    return {
        current: searchParams.pageNum ?? 1,
        pageSize: searchParams.pageSize ?? 4,
        total: total.value,
        showSizeChanger: true,
        showTotal: (total: number) => `共 ${total} 条`,
    }
})

const fetchFormData = async () => {
    const res = await listUserVoByPage(searchParams)
    if (res.data?.code === 0 && res.data?.data) {
        data.value = res.data?.data?.records ?? []
        total.value = res.data?.data?.totalRow ?? 0
    } else {
        message.error("获取数据失败," + res?.data?.message)
    }
}

// 表格变化处理
const doTableChange = (page: any) => {
    searchParams.pageNum = page.current
    searchParams.pageSize = page.pageSize
    fetchFormData()
}

const doSearch = () => {
    searchParams.pageNum = 1;
    fetchFormData();
}

const doDelete = async (id: number) => {
    if (!id) return
    const res = await deleteUser({ id });
    if (res.data?.code === 0) {
        message.success("删除成功")
        fetchFormData();
    } else {
        message.error("删除失败," + res.data.message)
    }
}

// 可编辑数据
const editableData: UnwrapRef<Record<number, API.UserVO>> = reactive({});
// 编辑
const edit = (id: number) => {
    editableData[id] = cloneDeep(data.value.filter(item => id === item.id)[0]);
};
// 保存
const save = async (id: number) => {
    const res = await update(editableData[id]);
    if (res.data?.code !== 0) {
        message.error("保存失败," + res.data?.message);
        return;
    } else {
        message.success("保存成功");
    }
    Object.assign(data.value.filter(item => id === item.id)[0], editableData[id]);
    delete editableData[id];
};
// 取消编辑
const cancel = (id: number) => {
    delete editableData[id];
};

onMounted(() => {
    fetchFormData()
})

</script>

<style scoped>
.editable-row-operations a {
    margin-right: 8px;
}
</style>