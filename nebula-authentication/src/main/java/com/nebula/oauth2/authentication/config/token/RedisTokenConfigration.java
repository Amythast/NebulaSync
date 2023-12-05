package com.nebula.oauth2.authentication.config.token;

import com.nebula.oauth2.authentication.consts.CacheConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * Redis token 配置
 *
 * @author feifeixia
 * 2019/7/15 16:04
 */
public class RedisTokenConfigration {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public TokenStore tokenStore() {
        final RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setPrefix(CacheConstants.REDIS_TOKEN_PREFIX);
        return tokenStore;
    }
}
