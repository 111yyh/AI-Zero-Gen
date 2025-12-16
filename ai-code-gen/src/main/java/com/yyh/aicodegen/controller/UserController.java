package com.yyh.aicodegen.controller;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.paginate.Page;
import com.yyh.aicodegen.annotation.AuthCheck;
import com.yyh.aicodegen.common.BaseResponse;
import com.yyh.aicodegen.common.DeleteRequest;
import com.yyh.aicodegen.common.ResultUtils;
import com.yyh.aicodegen.constant.UserConstant;
import com.yyh.aicodegen.exception.BusinessException;
import com.yyh.aicodegen.exception.ErrorCode;
import com.yyh.aicodegen.exception.ThrowUtils;
import com.yyh.aicodegen.model.dto.*;
import com.yyh.aicodegen.model.enums.UserRole;
import com.yyh.aicodegen.model.vo.LoginUserVO;
import com.yyh.aicodegen.model.vo.UserVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import com.yyh.aicodegen.model.entity.User;
import com.yyh.aicodegen.service.UserService;

import java.util.List;

/**
 * 用户 控制层。
 *
 * @author yyh
 * @since 1.0.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, new BusinessException(ErrorCode.PARAMS_ERROR));
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long res = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(res);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, new BusinessException(ErrorCode.PARAMS_ERROR));
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 获取当前登录用户信息
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, new BusinessException(ErrorCode.PARAMS_ERROR));
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, new BusinessException(ErrorCode.PARAMS_ERROR));
        return ResultUtils.success(userService.userLogout(request));
    }

    /**
     * 保存用户。
     *
     * @param userAddRequest
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, new BusinessException(ErrorCode.PARAMS_ERROR));
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        // 默认密码
        String userPassword = "123456789";
        String encryptPassword = userService.getEncryptPassword(userPassword);
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, new BusinessException(ErrorCode.OPERATION_ERROR));
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户。
     *
     * @param deleteRequest
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null && deleteRequest.getId() < 0,
                new BusinessException(ErrorCode.PARAMS_ERROR));
        long id = deleteRequest.getId();
        boolean result = userService.removeById(id);
        ThrowUtils.throwIf(!result, new BusinessException(ErrorCode.OPERATION_ERROR));
        return ResultUtils.success(result);
    }

    /**
     * 根据主键更新用户。
     *
     * @param userUpdateRequest
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> update(@RequestBody UserUpdateRequest userUpdateRequest) {
        ThrowUtils.throwIf(userUpdateRequest == null && userUpdateRequest.getId() == null,
                new BusinessException(ErrorCode.PARAMS_ERROR));
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, new BusinessException(ErrorCode.OPERATION_ERROR));
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(Long id) {
        ThrowUtils.throwIf(id == null, new BusinessException(ErrorCode.PARAMS_ERROR));
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, new BusinessException(ErrorCode.NOT_FOUND_ERROR));
        return ResultUtils.success(user);
    }

    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVO(Long id) {
        BaseResponse<User> userResponse = getUserById(id);
        User user = userResponse.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页查询用户。
     *
     * @param userQueryRequest 分页对象
     * @return 分页对象
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, new BusinessException(ErrorCode.PARAMS_ERROR));
        int pageNum = userQueryRequest.getPageNum();
        int pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(Page.of(pageNum, pageSize), userService.getQueryWrapper(userQueryRequest));
        List<User> userList = userPage.getRecords();
        // 数据脱敏
        Page<UserVO> userVOPage = new Page<>(pageNum, pageSize, userPage.getTotalPage());
        List<UserVO> userVOList = userService.getUserVOList(userList);
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

}
