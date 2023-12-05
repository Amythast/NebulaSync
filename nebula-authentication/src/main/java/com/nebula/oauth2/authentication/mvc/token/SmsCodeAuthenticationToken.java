package com.nebula.oauth2.authentication.mvc.token;

import com.nebula.oauth2.authentication.core.MyAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 定义验证码登录系统
 *
 * @author feifeixia
 * 2019/7/12 14:04
 * @see UsernamePasswordAuthenticationToken
 */
public class SmsCodeAuthenticationToken extends MyAuthenticationToken {

    private static final long serialVersionUID = 6056078576992222156L;

    public SmsCodeAuthenticationToken(final Object principal, final Object credentials) {
        super(principal, credentials);
    }

    public SmsCodeAuthenticationToken(final Object principal, final Object credentials, final Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
