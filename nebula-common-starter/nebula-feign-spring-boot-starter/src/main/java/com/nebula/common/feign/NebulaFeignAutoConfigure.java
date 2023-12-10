package com.nebula.common.feign;

import com.nebula.common.redis.template.NebulaRedisRepository;
import com.nebula.common.security.component.OAuth2RestTemplateWithScope;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.security.oauth2.client.OAuth2ClientContext;

/**
 * Feign统一配置
 *
 * @author feifeixia
 * @date 2018/9/18 14:04
 */
@Configuration
public class NebulaFeignAutoConfigure {

    /**
     * Feign 日志级别
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestHeaderInterceptor requestHeaderInterceptor() {
        return new RequestHeaderInterceptor();
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(
            @Qualifier("oauth2ClientContext") OAuth2ClientContext oAuth2ClientContext,
            OAuth2RestTemplateWithScope oAuth2RestTemplateWithScope,
            NebulaRedisRepository redisRepository
    ) {
        return new FeignClientInterceptor(
                oAuth2ClientContext,
                oAuth2RestTemplateWithScope,
                redisRepository
        );
    }
    @Bean
    public RequestAttributeHystrixConcurrencyStrategy hystrixRequestAutoConfiguration() {
        return new RequestAttributeHystrixConcurrencyStrategy();
    }
}
