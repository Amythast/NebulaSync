package com.nebula.common.feign;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.nebula.common.redis.template.NebulaRedisRepository;
import com.nebula.common.security.component.OAuth2RestTemplateWithScope;
import com.nebula.common.security.consts.SecurityConstants;
import com.nebula.common.utils.JsonUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Collection;

import static com.nebula.common.security.consts.CacheConstants.FEIGN_CLIENT_DETAILS_KEY;

/**
 * @author feifeixia
 * @date 2023/12/9-20:03
 */
public class FeignClientInterceptor implements RequestInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";
    private final OAuth2ClientContext oAuth2ClientContext;
    private final OAuth2RestTemplateWithScope oAuth2RestTemplateWithScope;
    private final NebulaRedisRepository redisRepository;
    public FeignClientInterceptor(OAuth2ClientContext oAuth2ClientContext,
                                  OAuth2RestTemplateWithScope oAuth2RestTemplateWithScope,
                                  NebulaRedisRepository redisRepository) {
        this.oAuth2ClientContext = oAuth2ClientContext;
        this.oAuth2RestTemplateWithScope = oAuth2RestTemplateWithScope;
        this.redisRepository = redisRepository;
    }

    @Override
    public void apply(RequestTemplate template) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
            template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, details.getTokenValue()));
        }

        Collection<String> fromHeader = template.headers().get(SecurityConstants.FROM);
        if (CollUtil.isNotEmpty(fromHeader) && fromHeader.contains(SecurityConstants.FROM_IN)) {
            if (oAuth2ClientContext != null && oAuth2ClientContext.getAccessToken() != null) {
                template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, oAuth2ClientContext.getAccessToken()));
            } else {
                OAuth2AccessToken accessToken = JSON.parseObject(redisRepository.get(FEIGN_CLIENT_DETAILS_KEY), OAuth2AccessToken.class);
                if (accessToken == null) {
                    accessToken = oAuth2RestTemplateWithScope.getAccessTokenForFeign();
                    redisRepository.set(FEIGN_CLIENT_DETAILS_KEY, JsonUtils.toJsonString(accessToken));
                }
                template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, accessToken.getValue()));
            }
        }
    }
}
