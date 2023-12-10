package com.nebula.gateway.zuul.handler;

import com.nebula.common.security.component.handler.ResourceAuthExceptionEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义token校验失败返回信息
 *
 * @author feifeixia
 * 2019/5/6 10:54
 */
@Component
public class CustomerExceptionEntryPoint extends ResourceAuthExceptionEntryPoint {

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException {
        super.commence(request, response, authException);
    }
}
