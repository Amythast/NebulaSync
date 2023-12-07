package com.nebula.common.redis;

import com.nebula.common.redis.template.NebulaRedisRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis 配置类
 *
 * @author feifeixia
 * @date 2017/11/6 11:02
 */
@Configuration
@ConditionalOnClass(NebulaRedisRepository.class)
@EnableConfigurationProperties(RedisProperties.class)
public class NebulaRedisAutoConfigure {

    /**
     * Redis repository redis repository.
     *
     * @param redisTemplate the redis template
     * @return the redis repository
     */
    @Bean
    @ConditionalOnMissingBean
    public NebulaRedisRepository redisRepository(RedisTemplate<String, String> redisTemplate) {
        return new NebulaRedisRepository(redisTemplate);
    }
}
