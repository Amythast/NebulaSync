package com.nebula.oauth2.authentication.config;

import com.nebula.oauth2.authentication.config.redis.NebulaRedisRepository;
import com.nebula.oauth2.authentication.consts.CacheConstants;
import com.nebula.oauth2.authentication.consts.SecurityConstants;
import com.nebula.oauth2.authentication.entity.User;
import com.nebula.oauth2.authentication.mvc.handler.CustomAccessDeniedHandler;
import com.nebula.oauth2.authentication.mvc.handler.CustomExceptionEntryPoint;
import com.nebula.oauth2.authentication.oauth2.code.RedisAuthenticationCodeServices;
import com.nebula.oauth2.authentication.oauth2.exception.CustomWebResponseExceptionTranslator;
import com.nebula.oauth2.authentication.oauth2.provider.CustomClientDetailsService;
import com.nebula.oauth2.authentication.service.user.UserNameUserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 认证服务器配置抽象
 *
 * @author feifeixia
 * @date 2018/7/24 16:10
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TarocoOauth2Properties oauth2Properties;

    @Autowired
    private NebulaRedisRepository redisRepository;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private UserNameUserDetailsServiceImpl userNameUserDetailsService;

    @Autowired
    private CustomWebResponseExceptionTranslator exceptionTranslator;

    @Autowired
    private CustomExceptionEntryPoint exceptionEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TokenStore tokenStore;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    /**
     * 授权服务器端点配置，如令牌存储，令牌自定义，用户批准和授权类型，不包括端点安全配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userNameUserDetailsService)
                .tokenServices(defaultTokenServices())
                .exceptionTranslator(exceptionTranslator)
                .authorizationCodeServices(redisAuthenticationCodeServices());
    }

    /**
     * 授权服务器端点的安全配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()")
                //allowFormAuthenticationForClients是为了注册clientCredentialsTokenEndpointFilter
                //clientCredentialsTokenEndpointFilter,解析request中的client_id和client_secret
                //构造成UsernamePasswordAuthenticationToken,然后通过UserDetailsService查询作简单的认证而已
                //一般是针对password模式和client_credentials 主要是让/oauth/token支持client_id以及client_secret作登录认证
                .allowFormAuthenticationForClients()
                .authenticationEntryPoint(exceptionEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }

    /**
     * 客户端管理
     */
    @Bean
    @Lazy
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    public ClientDetailsService clientDetails() {
        return new CustomClientDetailsService(CacheConstants.REDIS_CLIENTS_PREFIX, dataSource, redisRepository, objectMapper);
    }

    /**
     * 自定义AuthorizationCodeServices实现类来将auth_code 存放在redis中
     */
    @Bean
    public RedisAuthenticationCodeServices redisAuthenticationCodeServices() {
        return new RedisAuthenticationCodeServices(redisConnectionFactory);
    }

    /**
     * jwt 生成token 定制化处理
     * <p>
     * 额外信息（这部分信息不关乎加密方式）, 添加到随token一起的additionalInformation当中
     *
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Authentication userAuthentication = authentication.getUserAuthentication();
            if (userAuthentication == null) {
                return accessToken;
            }
            Map<String, Object> additionalInfo = new LinkedHashMap<>(accessToken.getAdditionalInformation());
            final Object principal = userAuthentication.getPrincipal();
            User user;
            if (principal instanceof User) {
                user = (User) principal;
            } else {
                final String username = (String) principal;
                user = (User) userNameUserDetailsService.loadUserByUsername(username);
            }
            additionalInfo.put(SecurityConstants.LICENSE_KEY, SecurityConstants.LICENSE);
            additionalInfo.put(SecurityConstants.USER_NAME_HEADER, user.getUsername());
            additionalInfo.put(SecurityConstants.USER_ID_HEADER, user.getUserId());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    @Bean
    @Lazy
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setAccessTokenValiditySeconds(oauth2Properties.getAccessTokenValiditySeconds());
        defaultTokenServices.setRefreshTokenValiditySeconds(oauth2Properties.getRefreshTokenValiditySeconds());
        defaultTokenServices.setClientDetailsService(clientDetails());

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        if (jwtAccessTokenConverter != null) {
            tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter, tokenEnhancer()));
        } else {
            tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(tokenEnhancer()));
        }
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);

        return defaultTokenServices;
    }
}
