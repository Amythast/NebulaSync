package com.nebula.rbac.admin.common.listener;

import com.nebula.common.constants.CacheConstants;
import com.nebula.common.constants.CommonConstant;
import com.nebula.common.entity.SysRoute;
import com.nebula.common.redis.template.NebulaRedisRepository;
import com.nebula.common.utils.JsonUtils;
import com.nebula.rbac.admin.service.SysRouteService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * @author feifeixia
 * @date 2018/5/16
 */
@Slf4j
@Component
public class RouteConfigInitListener implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private NebulaRedisRepository redisRepository;

    @Autowired
    private SysRouteService sysRouteService;

    /**
     * Callback used to run the bean.
     * 初始化路由配置的数据，避免gateway 依赖业务模块
     */
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("开始初始化路由配置数据");
        QueryWrapper<SysRoute> wrapper = new QueryWrapper<>();
        wrapper.eq(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        List<SysRoute> routeList = sysRouteService.list(wrapper);
        if (!CollectionUtils.isEmpty(routeList)) {
            redisRepository.set(CacheConstants.ROUTE_KEY, JsonUtils.toJsonString(routeList));
            log.info("更新Redis中路由配置数据：{}条", routeList.size());
        }
        log.info("初始化路由配置数据完毕");
    }
}
