package com.nebula.oauth2.authentication.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.GrantedAuthority;

/**
 * 操作权限
 *
 * @author feifeixia
 * 2019/7/16 15:15
 * @see SecurityExpressionRoot
 */
@Data
@AllArgsConstructor
public class Operation implements GrantedAuthority {

    private static final long serialVersionUID = 6260083887682221456L;

    private static final String PREFIX = "OP_";

    private String op;

    @Override
    public String getAuthority() {
        return PREFIX + op.toUpperCase();
    }
}
