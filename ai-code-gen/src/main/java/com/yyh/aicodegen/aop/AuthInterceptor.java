package com.yyh.aicodegen.aop;

import com.yyh.aicodegen.annotation.AuthCheck;
import com.yyh.aicodegen.constant.UserConstant;
import com.yyh.aicodegen.exception.BusinessException;
import com.yyh.aicodegen.exception.ErrorCode;
import com.yyh.aicodegen.exception.ThrowUtils;
import com.yyh.aicodegen.model.entity.User;
import com.yyh.aicodegen.model.enums.UserRole;
import com.yyh.aicodegen.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        UserRole mustRoleEnum = UserRole.getUserRole(mustRole);
        // 不需要权限，放行
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        User loginUser = userService.getLoginUser(request);
        UserRole loginUserRole = UserRole.getUserRole(loginUser.getUserRole());
        // 登录的用户没有角色，抛异常
        ThrowUtils.throwIf(loginUserRole == null,
                new BusinessException(ErrorCode.NO_AUTH_ERROR));
        // 需要管理员权限，登录用户不是管理员时，抛异常
        ThrowUtils.throwIf(UserRole.ADMIN.equals(mustRoleEnum) && !UserRole.ADMIN.equals(loginUserRole),
                new BusinessException(ErrorCode.NO_AUTH_ERROR));
        return joinPoint.proceed();
    }
}
