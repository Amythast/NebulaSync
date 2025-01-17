package com.nebula.oauth2.authentication.config;

import com.nebula.oauth2.authentication.oauth2.configurer.MobileTokenAuthenticationSecurityConfiguration;
import com.nebula.oauth2.authentication.mvc.configurer.SmsCodeAuthenticationSecurityConfigration;
import com.nebula.oauth2.authentication.mvc.filter.CustomUsernamePasswordAuthenticationFilter;
import com.nebula.oauth2.authentication.mvc.handler.CustomAccessDeniedHandler;
import com.nebula.oauth2.authentication.mvc.handler.CustomExceptionEntryPoint;
import com.nebula.oauth2.authentication.mvc.handler.UsernamePasswordAuthenticationFailureHandler;
import com.nebula.oauth2.authentication.mvc.handler.UsernamePasswordAuthenticationSuccessHandler;
import com.nebula.oauth2.authentication.mvc.handler.UsernamePasswordLogoutSuccessHandler;
import com.nebula.oauth2.authentication.service.user.UserNameUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SpringBootWebSecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

/**
 * webSecurity 权限控制类
 *
 * @author feifeixia
 * @date 2018/7/24 15:58
 * @see SecurityFilterAutoConfiguration
 * @see SpringBootWebSecurityConfiguration
 */
@EnableWebSecurity
public class WebSecurityConfigration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private NebulaOauth2Properties oauth2Properties;

    @Autowired
    private UserNameUserDetailsServiceImpl userNameUserDetailsService;

    @Autowired
    private UsernamePasswordAuthenticationSuccessHandler successHandler;

    @Autowired
    private UsernamePasswordAuthenticationFailureHandler failureHandler;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomExceptionEntryPoint exceptionEntryPoint;

    @Autowired
    private UsernamePasswordLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private MobileTokenAuthenticationSecurityConfiguration mobileTokenAuthenticationSecurityConfiguration;

    @Autowired
    private SmsCodeAuthenticationSecurityConfigration smsCodeAuthenticationSecurityConfigration;

    private static final String RM_KEY = UUID.randomUUID().toString();

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
                http
                        // 默认的用户名密码认证器
                        .authenticationProvider(daoAuthenticationProvider())
                        .apply(mobileTokenAuthenticationSecurityConfiguration)
                        .and()
                        .apply(smsCodeAuthenticationSecurityConfigration)
                        .and()
                        .addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                        .formLogin().loginPage("/").permitAll()
                        .loginProcessingUrl("/login").permitAll()
                        .and().logout().logoutUrl("/logout").permitAll().logoutSuccessHandler(logoutSuccessHandler)
                        // 异常处理filter: ExceptionTranslationFilter
                        .and().exceptionHandling()
                        // 匿名用户访问无权限资源时的异常
                        //.authenticationEntryPoint(exceptionEntryPoint)
                        // 认证过的用户访问无权限资源时的异常
                        .accessDeniedHandler(accessDeniedHandler)
                        // 开启RememberMe
                        .and().rememberMe().key(RM_KEY).rememberMeServices(rememberMeServices())
                        .and().authorizeRequests();

        final List<String> urlPermitAll = oauth2Properties.getUrlPermitAll();
        urlPermitAll.forEach(url -> registry.antMatchers(url).permitAll());
        registry.anyRequest().authenticated().and().cors().and().csrf().disable();
    }

    /**
     * 加密器 spring boot 2.x没有默认的加密器了
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 默认的用户名密码 AuthenticationProvider
     *
     * @return
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userNameUserDetailsService);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * 注册自定义的UsernamePasswordAuthenticationFilter
     *
     * @throws Exception
     */
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setRememberMeServices(rememberMeServices());
        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    /**
     * RememberMeServices
     */
    public RememberMeServices rememberMeServices() {
        final JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return new PersistentTokenBasedRememberMeServices(RM_KEY, userNameUserDetailsService, tokenRepository);
    }
}
