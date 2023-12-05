package com.nebula.oauth2.authentication.mvc.configurer;

import com.nebula.oauth2.authentication.config.redis.NebulaRedisRepository;
import com.nebula.oauth2.authentication.mvc.filter.SmsCodeAuthenticationFilter;
import com.nebula.oauth2.authentication.mvc.handler.UsernamePasswordAuthenticationFailureHandler;
import com.nebula.oauth2.authentication.mvc.handler.UsernamePasswordAuthenticationSuccessHandler;
import com.nebula.oauth2.authentication.mvc.provider.SmsCodeAuthenticationProvider;
import com.nebula.oauth2.authentication.config.redis.NebulaRedisRepository;
import com.nebula.oauth2.authentication.service.user.MobileUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 手机号/验证码登录 安全配置
 *
 * @author feifeixia
 * 2019/7/22 16:14
 */
@Component
public class SmsCodeAuthenticationSecurityConfigration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private NebulaRedisRepository redisRepository;

    @Autowired
    private MobileUserDetailsService mobileUserDetailsService;

    @Autowired
    private UsernamePasswordAuthenticationSuccessHandler successHandler;

    @Autowired
    private UsernamePasswordAuthenticationFailureHandler failureHandler;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        final SmsCodeAuthenticationFilter filter = new SmsCodeAuthenticationFilter();
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);

        final SmsCodeAuthenticationProvider provider = new SmsCodeAuthenticationProvider();
        provider.setRedisRepository(redisRepository);
        provider.setUserDetailsService(mobileUserDetailsService);
        provider.setHideUserNotFoundExceptions(false);

        http
                .authenticationProvider(provider)
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
