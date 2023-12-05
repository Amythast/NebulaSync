package com.nebula.common.ribbon;

import org.springframework.cloud.netflix.ribbon.DefaultPropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ribbon扩展配置类
 *
 * @author feifeixia
 * @date 2017/11/17 9:24
 */
@Configuration
public class TarocoRibbonAutoConfigure {

    @Bean
    public DefaultPropertiesFactory defaultPropertiesFactory() {
        return new DefaultPropertiesFactory();
    }
}
