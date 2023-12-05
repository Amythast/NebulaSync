package com.nebula.gateway.zuul.handler;

import com.nebula.common.constants.CommonConstant;
import com.nebula.common.exception.DefaultError;
import com.nebula.common.utils.JsonUtils;
import com.nebula.common.web.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
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
public class CustomerAccessDeniedHandler extends OAuth2AccessDeniedHandler {

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
        log.info("授权失败，禁止访问 {}", request.getRequestURI());
        response.setCharacterEncoding(CommonConstant.UTF8);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Response result = Response.failure(DefaultError.ACCESS_DENIED);
        response.setStatus(HttpStatus.SC_FORBIDDEN);
        response.getWriter().write(JsonUtils.toJsonString(result));
    }
}
