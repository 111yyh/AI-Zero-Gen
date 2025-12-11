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

    long userRegister(String userAccount, String userPassword, String checkPassword);

    String getEncryptPassword(String password);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

}
