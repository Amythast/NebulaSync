package com.nebula.oauth2.authentication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * nebula oauth2 配置类
 *
 * @author feifeixia
 * 2018/12/4 15:34
 */
@Component
@ConfigurationProperties("nebula.oauth2")
@Data
public class NebulaOauth2Properties {

    /**
     * accessTokenValiditySeconds, default: 1 day.
     */
    private int accessTokenValiditySeconds = 60 * 60 * 24;

    /**
     * refreshTokenValiditySeconds, default: 1 day.
     */
    private int refreshTokenValiditySeconds = 60 * 60 * 24;

    /**
     * the urls for permitAll.
     */
    private List<String> urlPermitAll = new ArrayList<>();

    /**
     * The key store properties for locating a key in a Java Key Store (a file in a format
     * defined and understood by the JVM).
     */
    private KeyStore keyStore = new KeyStore();

    @Data
    public static class KeyStore {

        /**
         * Location of the key store file, e.g. classpath:/keystore.jks.
         */
        private Resource location;

        /**
         * Password that locks the keystore.
         */
        private String password;

        /**
         * Alias for a key in the store.
         */
        private String alias;

        /**
         * Secret protecting the key (defaults to the same as the password).
         */
        private String secret;

    }
}
