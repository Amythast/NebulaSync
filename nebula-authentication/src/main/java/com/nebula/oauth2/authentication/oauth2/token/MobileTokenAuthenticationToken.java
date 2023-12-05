package com.nebula.oauth2.authentication.oauth2.token;

import com.nebula.oauth2.authentication.core.MyAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 定义手机号获取token 类似与 UsernamePasswordAuthenticationToken
 *
 * @author feifeixia
 * 2019/5/13 15:20
 * @see UsernamePasswordAuthenticationToken
 */
public class MobileTokenAuthenticationToken extends MyAuthenticationToken {

    private static final long serialVersionUID = -9192173797915966518L;

    public MobileTokenAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public MobileTokenAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
