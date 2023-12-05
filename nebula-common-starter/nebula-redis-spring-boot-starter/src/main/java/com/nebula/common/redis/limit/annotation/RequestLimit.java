package com.nebula.common.redis.limit.annotation;

import java.lang.annotation.*;

/**
 * redis请求限流 Controller注解
 *
 * @author feifeixia
 * @date 2018/5/21 13:54
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestLimit {

    /**
     * Error code
     * @return
     * code
     */
    int errorCode() default 500;

    /**
     * Error Message
     * @return
     * message
     */
    String errorMsg() default "request limited";
}
