package com.nebula.common.redis.limit;

import com.nebula.common.redis.util.LimitScriptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;

/**
 * redis 请求限流
 *
 * @author feifeixia
 * @date 2018/5/21 11:52
 */
@Slf4j
public class RedisLimit {

    private final StringRedisTemplate redisTemplate;
    private final int limit;
    private static final int FAIL_CODE = 0;

    /**
     * lua script
     */
    private String script;

    private RedisLimit(Builder builder) {
        this.limit = builder.limit;
        this.redisTemplate = builder.redisTemplate;
        buildScript();
    }


    /**
     * limit traffic
     *
     * @return if true
     */
    public boolean limit() {

        Object result = limitRequest();

        return FAIL_CODE != (Long) result;
    }

    private Object limitRequest() {
        String key = String.valueOf(System.currentTimeMillis() / 1000);
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        return redisTemplate.execute(redisScript, Collections.singletonList(key), Collections.singletonList(String.valueOf(limit)));
    }

    /**
     * read lua script
     */
    private void buildScript() {
        script = LimitScriptUtil.getScript("limit.lua");
    }


    /**
     * the builder
     */
    public static class Builder {
        private final StringRedisTemplate redisTemplate;

        private int limit = 200;

        public Builder(StringRedisTemplate redisTemplate) {
            this.redisTemplate = redisTemplate;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public RedisLimit build() {
            return new RedisLimit(this);
        }

    }
}
