import { checkAccess } from "@/common/checkAccess";
import { ACCESS_ENUM } from "@/enum/accessEnum";
import router from "@/router";
import { useLoginUserStore } from "@/stores/loginUser";

router.beforeEach(async (to, from, next) => {
    const loginUserStore = useLoginUserStore();
    let loginUser = loginUserStore.loginUser;

    // 如果之前没登陆过，自动登录
    if (!loginUser || !loginUser.userRole) {
        // 加 await 是为了等用户登录成功之后，再执行后续的代码
        await loginUserStore.fetchLoginUser();
        loginUser = loginUserStore.loginUser;
    }

    const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.NOT_LOGIN;
    console.log(needAccess);

    // 跳转的页面需要登录
    if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
        // 未登录
        if (!loginUser || !loginUser.userRole || loginUser.userRole === ACCESS_ENUM.NOT_LOGIN) {
            next(`/user/login?redirect=${to.fullPath}`);
            return;
        }
    }
    // 权限不够
    if (!checkAccess(loginUser, needAccess)) {
        next("/noauth");
        return;
    }
    next();
})