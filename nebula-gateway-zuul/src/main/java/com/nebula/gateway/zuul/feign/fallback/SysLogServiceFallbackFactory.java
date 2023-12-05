package com.nebula.gateway.zuul.feign.fallback;

import com.nebula.gateway.zuul.feign.SysLogService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * desc
 *
 * @author feifeixia
 * @create 2020-07-20 14:07
 */
@Slf4j
@Component
public class SysLogServiceFallbackFactory implements FallbackFactory<SysLogService> {
    @Override
    public SysLogService create(final Throwable t) {
        return sysLog -> log.error("fallback for SysLogService.add; reason was: " + t.getMessage());
    }
}
