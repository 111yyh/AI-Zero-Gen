package com.yyh.aicodegen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yyh.aicodegen.exception.BusinessException;
import com.yyh.aicodegen.exception.ErrorCode;
import com.yyh.aicodegen.model.dto.app.AppQueryRequest;
import com.yyh.aicodegen.model.entity.App;
import com.yyh.aicodegen.mapper.AppMapper;
import com.yyh.aicodegen.model.entity.User;
import com.yyh.aicodegen.model.vo.AppVO;
import com.yyh.aicodegen.model.vo.UserVO;
import com.yyh.aicodegen.service.AppService;
import com.yyh.aicodegen.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用 服务层实现。
 *
 * @author yyh
 * @since 1.0.0
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService{

    @Resource
    private UserService userService;

    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtil.copyProperties(app, appVO);
        Long userId = app.getUserId();
        if (userId != null) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            appVO.setUserVO(userVO);
        }
        return appVO;
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest) {
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String cover = appQueryRequest.getCover();
        String initPrompt = appQueryRequest.getInitPrompt();
        String codeGenType = appQueryRequest.getCodeGenType();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();
        QueryWrapper queryWrapper = new QueryWrapper();
        if (id != null) queryWrapper.eq("id", id);
        if (appName != null) queryWrapper.like("app_name", appName);
        if (cover != null) queryWrapper.like("cover", cover);
        if (initPrompt != null) queryWrapper.like("initPrompt", initPrompt);
        if (codeGenType != null) queryWrapper.eq("codeGenType", codeGenType);
        if (priority != null) queryWrapper.eq("priority", priority);
        if (userId != null) queryWrapper.eq("userId", userId);
        if (sortField != null && !sortField.isEmpty()) queryWrapper.orderBy(sortField, "ascend".equals(sortOrder));
        return queryWrapper;
    }

    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        if (CollUtil.isEmpty(appList)) {
            return new ArrayList<>();
        }
        // 批量获取用户信息
        Set<Long> userIdSet = appList.stream()
                .map(App::getUserId)
                .collect(Collectors.toSet());
        Map<Long, UserVO> userVOMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.toMap(User::getId, userService::getUserVO));
        return appList.stream().map(app -> {
            AppVO appVO = getAppVO(app);
            appVO.setUserVO(userVOMap.get(app.getUserId()));
            return appVO;
        }).collect(Collectors.toList());
    }
}
