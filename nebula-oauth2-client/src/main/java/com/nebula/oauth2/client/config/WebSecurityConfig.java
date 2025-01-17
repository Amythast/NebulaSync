package com.nebula.oauth2.client.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * 客户端的Security配置
 *
 * @author feifeixia
 * 2019/5/6 17:54
 * @see OAuth2ClientAuthenticationProcessingFilter
 */
@Configuration
@EnableOAuth2Sso
@Order(value = 0)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 定义 OAuth2RestTemplate 访问资源服务器资源自动带上授权的token
     *
     * @param oAuth2ClientContext
     * @param details
     * @return
     */
    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oAuth2ClientContext,
                                                 OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oAuth2ClientContext);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.logout()
            .logoutSuccessUrl("http://localhost:9001/oauth/exit")
            .and()
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .anyRequest().authenticated();
    }
}
