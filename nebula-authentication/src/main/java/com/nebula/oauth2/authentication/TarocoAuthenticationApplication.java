package com.nebula.oauth2.authentication;

import com.nebula.oauth2.authentication.config.annotation.EnableJwtToken;
import com.nebula.oauth2.authentication.consts.CacheConstants;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 启动类
 *
 * @author feifeixia
 * 2019/7/2 16:11
 */
@EnableJwtToken
@EnableFeignClients
@EnableDiscoveryClient
@EnableRedisHttpSession(redisNamespace = CacheConstants.REDIS_SESSION_PREFIX)
@SpringBootApplication
public class TarocoAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TarocoAuthenticationApplication.class, args);
    }

    /**
     * Mybatis plus 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 解决vue-router history 模式下刷新页面的404问题
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(){
        return factory -> {
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
            factory.addErrorPages(error404Page);
        };
    }
}
