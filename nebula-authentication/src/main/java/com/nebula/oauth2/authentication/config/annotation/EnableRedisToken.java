package com.nebula.oauth2.authentication.config.annotation;

import com.nebula.oauth2.authentication.config.token.RedisTokenConfigration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用redis token
 *
 * @author feifeixia
 * 2019/7/15 16:10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisTokenConfigration.class)
public @interface EnableRedisToken {
}
