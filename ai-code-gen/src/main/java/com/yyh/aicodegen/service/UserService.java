package com.yyh.aicodegen.service;

import com.mybatisflex.core.service.IService;
import com.yyh.aicodegen.model.entity.User;
import com.yyh.aicodegen.vo.LoginUserVO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户 服务层。
 *
 * @author yyh
 * @since 1.0.0
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 账号
     * @param userPassword 密码
     * @param checkPassword 重复密码
     * @return userId
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 使用MD5算法加密
     * @param password 密码
     * @return 加密密码
     */
    String getEncryptPassword(String password);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return 脱敏的用户对象
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 用户登录
     * @param userAccount 用户名
     * @param userPassword 密码
     * @param request 请求（用于获取Session来记录登录状态）
     * @return 脱敏的用户对象
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录的用户信息
     * @param request 请求
     * @return 脱敏的用户对象
     */
    LoginUserVO getLoginUser(HttpServletRequest request);

    /**
     * 用户注销
     * @param request 请求
     * @return 注销成功与否
     */
    boolean userLogout(HttpServletRequest request);
}
