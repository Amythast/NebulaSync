package com.nebula.common.web.annotation;

import com.nebula.common.constants.PermissionConst;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限拦截
 *
 * @author feifeixia
 * 2019/4/24 11:37
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /**
     * 权限 code
     *
     * @return
     */
    String value() default PermissionConst.ANY;

    /**
     * 超级管理员是否可以访问 默认: true
     *
     * @return
     */
    boolean isAdminAccess() default true;
}
