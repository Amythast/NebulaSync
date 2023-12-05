package com.nebula.gateway.zuul.filter.pre;

import cn.hutool.core.collection.CollUtil;
import com.nebula.common.constants.SecurityConstants;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;

/**
 * 将认证用户的相关信息 放入header中, 后端服务可以直接读取使用
 * 在RateLimitPreFilter 之前执行，不然有空指针问题
 *
 * @author feifeixia
 * @date 2017/11/20
 */
@Component
public class UserInfoHeaderFilter extends ZuulFilter {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FORM_BODY_WRAPPER_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            RequestContext requestContext = RequestContext.getCurrentContext();
            requestContext.addZuulRequestHeader(SecurityConstants.USER_HEADER, authentication.getName());
            requestContext.addZuulRequestHeader(SecurityConstants.USER_ROLE_HEADER, CollUtil.join(authentication.getAuthorities(), ","));
            String tokenValue = extractToken(request);
            if (!StringUtils.isEmpty(tokenValue)) {
                OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
                if (accessToken != null && !CollectionUtils.isEmpty(accessToken.getAdditionalInformation())) {
                    Map<String, Object> information = accessToken.getAdditionalInformation();
                    requestContext.addZuulRequestHeader(SecurityConstants.HEADER_LABEL, information.get(SecurityConstants.HEADER_LABEL) + "");
                    requestContext.addZuulRequestHeader(SecurityConstants.USER_PERMISSION_HEADER, information.get(SecurityConstants.USER_PERMISSION_HEADER) + "");
                }
            }
        }
        return null;
    }

    /**
     * 获取请求Bearer Token
     * @param request request对象
     * @return tokenValue
     */
    private String extractToken(HttpServletRequest request) {
        String token = extractHeaderToken(request);
        if (token == null) {
            token = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
        }
        return token;
    }

    /**
     * 请求头中获取Bearer Token
     *
     * @param request The request.
     * @return The token, or null if no OAuth authorization header was supplied.
     */
    private String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(SecurityConstants.AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }
}
