package com.nebula.common.security.consts;

/**
 * 缓存常量
 *
 * @author feifeixia
 * 2019/4/25 10:20
 */
public interface CacheConstants {

    /**
     * 缓存分隔符
     */
    String SPLIT = ":";

    /**
     * NEBULA缓存公共前缀
     */
    String PREFIX = "nebula" + SPLIT;

    /**
     * Redis session 缓存前缀
     */
    String REDIS_SESSION_PREFIX = PREFIX + "session";

    /**
     * Redis token 缓存前缀
     */
    String REDIS_TOKEN_PREFIX = PREFIX + "token" + SPLIT;

    /**
     * Redis 客户端 缓存前缀
     */
    String REDIS_CLIENTS_PREFIX = PREFIX + "clients" + SPLIT;

    /**
     * 验证码缓存key
     */
    String DEFAULT_CODE_KEY = PREFIX + "code" + SPLIT;

    /**
     * feign client token. Using for feign request
     */
    String FEIGN_CLIENT_DETAILS_KEY = PREFIX + "feign:client:token";


    /**
     * 默认过期时间 60秒
     */
    int DEFAULT_EXPIRE_SECONDS = 60;
}
