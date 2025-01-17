package com.nebula.common.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Feign 请求header传递
 *
 * @author feifeixia
 * 2019/4/25 16:58
 */
@Slf4j
public class RequestHeaderInterceptor implements RequestInterceptor {
    public RequestHeaderInterceptor() {
    }

    @Override
    public void apply(final RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
             while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                template.header(name, values);
            }
            log.debug("feign interceptor header:{}", template);
        }
    }
}
