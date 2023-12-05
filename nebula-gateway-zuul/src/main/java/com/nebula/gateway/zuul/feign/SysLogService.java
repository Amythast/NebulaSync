package com.nebula.gateway.zuul.feign;

import com.nebula.common.constants.ServiceNameConst;
import com.nebula.common.entity.SysLog;
import com.nebula.gateway.zuul.feign.fallback.SysLogServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 系统日志Service
 *
 * @author feifeixia
 * @date 2018/9/13 17:06
 */
@FeignClient(name = ServiceNameConst.RBAC_SERVICE, fallbackFactory = SysLogServiceFallbackFactory.class)
public interface SysLogService {

    /**
     * 添加日志
     *
     * @param log 日志实体
     */
    @PostMapping("/log")
    void add(@RequestBody SysLog log);
}
