package com.nebula.rbac.admin.service.impl;

import com.nebula.common.constants.CacheConstants;
import com.nebula.common.constants.CommonConstant;
import com.nebula.common.entity.SysRoute;
import com.nebula.common.redis.template.NebulaRedisRepository;
import com.nebula.common.utils.JsonUtils;
import com.nebula.rbac.admin.mapper.SysRouteMapper;
import com.nebula.rbac.admin.service.SysRouteService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 动态路由配置表 服务实现类
 *
 * @author feifeixia
 * @since 2018-05-15
 */
@Service
public class SysRouteServiceImpl extends ServiceImpl<SysRouteMapper, SysRoute> implements SysRouteService {

    @Autowired
    private NebulaRedisRepository redisRepository;

    /**
     * 同步路由配置信息,到服务网关
     *
     * @return 同步成功
     */
    @Override
    public Boolean applyZuulRoute() {
        QueryWrapper<SysRoute> wrapper = new QueryWrapper<>();
        wrapper.eq(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        List<SysRoute> routeList = list(wrapper);
        redisRepository.set(CacheConstants.ROUTE_KEY, JsonUtils.toJsonString(routeList));
        return Boolean.TRUE;
    }
}
