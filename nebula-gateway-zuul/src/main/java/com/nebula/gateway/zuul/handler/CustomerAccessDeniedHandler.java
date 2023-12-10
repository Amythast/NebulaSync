package com.nebula.gateway.zuul.handler;

import com.nebula.common.security.component.handler.BaseAccessDeniedHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权拒绝处理器，覆盖默认的OAuth2AccessDeniedHandler
 *
 * @author feifeixia
 * @date 2017/12/29
 */
@Slf4j
@Component
public class CustomerAccessDeniedHandler extends BaseAccessDeniedHandler {

    /**
     * 授权拒绝处理，使用R包装
     *
     * @param request       request
     * @param response      response
     * @param authException authException
     * @throws IOException      IOException
     * @throws ServletException ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException, ServletException {
        super.handle(request, response, authException);
    }
}
