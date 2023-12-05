package com.nebula.common.redis.limit.config;

import com.nebula.common.redis.limit.RedisLimit;
import com.nebula.common.redis.limit.intercept.SpringMvcIntercept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * redis 请求限流自动配置
 *
 * @author feifeixia
 * @date 2018/5/21 14:11
 */
@Configuration
@EnableConfigurationProperties(RedisLimitProperties.class)
@ConditionalOnProperty(name = "taroco.redis.limit.enabled", havingValue = "true")
public class RedisLimitAutoConfigurer implements WebMvcConfigurer {


    @Autowired
    private RedisLimitProperties limitProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Bean
    public RedisLimit redisLimit() {
        return new RedisLimit.Builder(redisTemplate)
                .limit(limitProperties.getValue())
                .build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SpringMvcIntercept(redisLimit()));
    }
}
