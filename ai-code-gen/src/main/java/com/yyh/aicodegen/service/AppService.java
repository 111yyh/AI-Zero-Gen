package com.yyh.aicodegen.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.yyh.aicodegen.model.dto.app.AppQueryRequest;
import com.yyh.aicodegen.model.dto.user.UserQueryRequest;
import com.yyh.aicodegen.model.entity.App;
import com.yyh.aicodegen.model.vo.AppVO;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author yyh
 * @since 1.0.0
 */
public interface AppService extends IService<App> {

    /**
     * 获取AppVO
     * @param app
     * @return
     */
    public AppVO getAppVO(App app);

    /**
     * 根据查询请求组装QueryWrapper
     * @param appQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 获取脱敏的App列表
     * @param appList
     * @return
     */
    public List<AppVO> getAppVOList(List<App> appList);
}
