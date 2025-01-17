package com.nebula.common.constants;

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
     * 路由信息Redis保存的key
     */
    String ROUTE_KEY = PREFIX + "routes";

    /**
     * 验证码缓存key
     */
    String DEFAULT_CODE_KEY = PREFIX + "code" + SPLIT;
}
