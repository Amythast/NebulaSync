package com.nebula.common.config;

import com.nebula.common.exception.DefaultExceptionAdvice;
import com.nebula.common.web.interceptor.PermissionInterceptor;
import com.nebula.common.web.interceptor.RoleInterceptor;
import com.nebula.common.web.resolver.TokenArgumentResolver;
import com.nebula.common.web.filter.RequestPerformanceFilter;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;

/**
 * taroco 公共配置类, 一些公共工具配置
 *
 * @author feifeixia
 * @date 2017/8/25
 */
@Configuration
public class TarocoCommonAutoConfig implements WebMvcConfigurer {

    /**
     * Token参数解析
     *
     * @param argumentResolvers 解析类
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new TokenArgumentResolver());
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new RoleInterceptor());
        registry.addInterceptor(new PermissionInterceptor());
    }

    @Bean
    @ConditionalOnMissingBean({DefaultExceptionAdvice.class})
    public DefaultExceptionAdvice defaultExceptionAdvice() {
        return new DefaultExceptionAdvice();
    }

    /**
     * 过滤器配置
     */
    @Bean
    @ConditionalOnClass(RequestPerformanceFilter.class)
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean<RequestPerformanceFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        RequestPerformanceFilter filter = new RequestPerformanceFilter();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addInitParameter("threshold","3000");
        filterRegistrationBean.addInitParameter("includeQueryString", "true");
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    /**
     * 定义 Validator bean
     * 一个校验失败就立即返回
     */
    @Bean
    public Validator validator() {
        return Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory()
                .getValidator();
    }
}
