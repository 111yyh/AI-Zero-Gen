import { ACCESS_ENUM } from "@/enum/accessEnum";

export const checkAccess = (loginUser: API.LoginUserVO, needAccess = ACCESS_ENUM.NOT_LOGIN) => {
    // 获取用户的角色（如果没有就是未登录）
    const loginUserAccess = loginUser?.userRole ?? ACCESS_ENUM.NOT_LOGIN;
    // 不需要权限
    if (needAccess === ACCESS_ENUM.NOT_LOGIN) return true;
    // 需要用户权限
    if (needAccess === ACCESS_ENUM.USER) {
        // 用户未登录，阻止
        if (loginUserAccess === ACCESS_ENUM.NOT_LOGIN) {
            return false;
        }
        return true;
    }
    // 需要管理员权限
    if (needAccess === ACCESS_ENUM.ADMIN) {
        // 用户未登录或者是普通用户，阻止
        if (loginUserAccess !== ACCESS_ENUM.ADMIN) {
            return false;
        }
        return true;
    }
}