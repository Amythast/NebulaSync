package com.nebula.gateway.zuul.filter.pre;

import cn.hutool.core.util.StrUtil;
import com.nebula.common.constants.CacheConstants;
import com.nebula.common.constants.SecurityConstants;
import com.nebula.common.exception.ValidateCodeException;
import com.nebula.common.redis.template.NebulaRedisRepository;
import com.nebula.common.utils.JsonUtils;
import com.nebula.common.web.Response;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 验证码验证
 *
 * @author feifeixia
 * @date 2018/5/10
 * <p>
 * security.validate.code 默认 为false，开启需要设置为true
 */
@Slf4j
@Component("validateCodeFilter")
@ConditionalOnProperty(value = "security.validate.code", havingValue = "true")
public class ValidateCodeFilter extends ZuulFilter {
    private static final String EXPIRED_CAPTCHA_ERROR = "验证码已过期，请重新获取";

    @Autowired
    private NebulaRedisRepository redisRepository;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_ERROR_FILTER_ORDER + 1;
    }

    /**
     * 是否校验验证码
     * 1. 判断验证码开关是否开启
     * 2. 判断请求是否登录请求
     * 3. 判断终端是否支持
     *
     * @return true/false
     */
    @Override
    public boolean shouldFilter() {
        final HttpServletRequest request = RequestContext.getCurrentContext().getRequest();

        if (RequestMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod())) {
            return false;
        }

        // 对指定的请求方法 进行验证码的校验
        return StrUtil.containsAnyIgnoreCase(request.getRequestURI(), SecurityConstants.OAUTH_TOKEN_URL);
    }

    @Override
    public Object run() {
        try {
            checkCode(RequestContext.getCurrentContext().getRequest());
        } catch (ValidateCodeException e) {
            final RequestContext ctx = RequestContext.getCurrentContext();
            final Response result = Response.failure(e.getMessage());
            final HttpServletResponse response = ctx.getResponse();

            response.setCharacterEncoding(Charset.defaultCharset().name());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(478);
            try {
                response.getWriter().print(JsonUtils.toJsonString(result));
            } catch (IOException e1) {
                log.error("response io异常");
                e1.printStackTrace();
            }
            ctx.setSendZuulResponse(false);
            ctx.setResponse(response);
        }
        return null;
    }

    /**
     * 检查code
     *
     * @param httpServletRequest request
     * @throws ValidateCodeException 验证码校验异常
     */
    private void checkCode(HttpServletRequest httpServletRequest) throws ValidateCodeException {
        final String code = httpServletRequest.getParameter("code");
        if (StrUtil.isBlank(code)) {
            throw new ValidateCodeException("请输入验证码");
        }

        String randomStr = httpServletRequest.getParameter("randomStr");
        if (StrUtil.isBlank(randomStr)) {
            randomStr = httpServletRequest.getParameter("mobile");
        }

        final String key = CacheConstants.DEFAULT_CODE_KEY + randomStr;
        if (!redisRepository.exists(key)) {
            throw new ValidateCodeException(EXPIRED_CAPTCHA_ERROR);
        }

        final Object codeObj = redisRepository.get(key);

        if (codeObj == null) {
            throw new ValidateCodeException(EXPIRED_CAPTCHA_ERROR);
        }

        final String saveCode = codeObj.toString();
        if (StrUtil.isBlank(saveCode)) {
            redisRepository.del(key);
            throw new ValidateCodeException(EXPIRED_CAPTCHA_ERROR);
        }

        if (!StrUtil.equals(saveCode, code)) {
            redisRepository.del(key);
            throw new ValidateCodeException("验证码错误，请重新输入");
        }

        redisRepository.del(key);
    }
}
