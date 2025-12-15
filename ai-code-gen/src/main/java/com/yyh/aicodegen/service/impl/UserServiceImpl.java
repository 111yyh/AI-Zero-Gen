package com.yyh.aicodegen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yyh.aicodegen.constant.UserConstant;
import com.yyh.aicodegen.exception.BusinessException;
import com.yyh.aicodegen.exception.ErrorCode;
import com.yyh.aicodegen.exception.ThrowUtils;
import com.yyh.aicodegen.model.entity.User;
import com.yyh.aicodegen.mapper.UserMapper;
import com.yyh.aicodegen.model.enums.UserRole;
import com.yyh.aicodegen.service.UserService;
import com.yyh.aicodegen.vo.LoginUserVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 用户 服务层实现。
 *
 * @author yyh
 * @since 1.0.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService{

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        ThrowUtils.throwIf(StrUtil.hasBlank(userAccount, userPassword, checkPassword),
                new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空"));
        ThrowUtils.throwIf(userAccount.length() < 4,
                new BusinessException(ErrorCode.PARAMS_ERROR, "用户名过短"));
        ThrowUtils.throwIf(userPassword.length() < 4 || checkPassword.length() < 4,
                new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短"));
        ThrowUtils.throwIf(!userPassword.equals(checkPassword),
                new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致"));
        // 2. 检查是否重复
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.mapper.selectCountByQuery(queryWrapper);
        ThrowUtils.throwIf(count > 0,
                new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复"));
        // 3. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("sb");
        user.setUserRole(UserRole.USER.getValue());
        boolean saveRes = this.save(user);
        ThrowUtils.throwIf(!saveRes, new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败"));
        return user.getId();
    }

    @Override
    public String getEncryptPassword(String password) {
        String salt = "yyh";
        return DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        ThrowUtils.throwIf(StrUtil.hasBlank(userAccount, userPassword),
                new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空"));
        ThrowUtils.throwIf(userAccount.length() < 4,
                new BusinessException(ErrorCode.PARAMS_ERROR, "用户名过短"));
        ThrowUtils.throwIf(userPassword.length() < 4,
                new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短"));
        // 2. 寻找用户
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        String encryptPassword = getEncryptPassword(userPassword);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.mapper.selectOneByQuery(queryWrapper);
        ThrowUtils.throwIf(user == null,
                new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码错误"));
        // 3. 记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        // 4. 返回脱敏数据
        return getLoginUserVO(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        ThrowUtils.throwIf(user == null || user.getId() == null,
                new BusinessException(ErrorCode.NOT_LOGIN_ERROR));
        // 保险起见，再从数据库中找一下
        User userInDb = this.getById(user.getId());
        ThrowUtils.throwIf(userInDb == null,
                new BusinessException(ErrorCode.NOT_LOGIN_ERROR));
        return user;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        Object user = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        ThrowUtils.throwIf(user == null,
                new BusinessException(ErrorCode.OPERATION_ERROR, "未登录"));
        // 移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }
}
