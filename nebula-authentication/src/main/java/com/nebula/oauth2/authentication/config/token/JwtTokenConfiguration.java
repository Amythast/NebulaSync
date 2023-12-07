package com.nebula.oauth2.authentication.config.token;

import com.nebula.oauth2.authentication.config.NebulaOauth2Properties;
import com.nebula.oauth2.authentication.oauth2.token.CustomerAccessTokenConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * Jwt token 配置
 *
 * @author feifeixia
 * 2019/7/15 15:57
 */
public class JwtTokenConfiguration {

    @Autowired
    private NebulaOauth2Properties oauth2Properties;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * JWT Token 生成转换器（加密方式以及加密的Token中存放哪些信息）
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory
                (oauth2Properties.getKeyStore().getLocation(), oauth2Properties.getKeyStore().getSecret().toCharArray())
                .getKeyPair(oauth2Properties.getKeyStore().getAlias());
        converter.setKeyPair(keyPair);
        converter.setAccessTokenConverter(new CustomerAccessTokenConverter());
        return converter;
    }
}
