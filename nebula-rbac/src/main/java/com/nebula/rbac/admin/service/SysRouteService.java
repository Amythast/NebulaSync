package com.nebula.rbac.admin.service;

import com.nebula.common.entity.SysRoute;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 动态路由配置表 服务类
 * </p>
 *
 * @author feifeixia
 * @since 2018-05-15
 */
public interface SysRouteService extends IService<SysRoute> {

    /**
     * 立即生效配置
     * @return
     */
    Boolean applyZuulRoute();
}
