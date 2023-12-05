package com.nebula.oauth2.authentication.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Security message
 *
 * @author feifeixia
 * @date 2018/7/30 17:32
 */
@Configuration
public class SecurityMessageLocal {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:org/springframework/security/messages_zh_CN");
        return messageSource;
    }
}
