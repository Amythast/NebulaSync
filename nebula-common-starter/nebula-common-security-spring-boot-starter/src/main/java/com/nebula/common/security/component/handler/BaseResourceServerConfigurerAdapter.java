/*
 *  Copyright (c) 2019-2020,.
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>

 */

package com.nebula.common.security.component.handler;

import com.nebula.common.config.FilterIgnorePropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.web.HeaderBearerTokenResolver;

/**
 * @author feifeixia
 * @date 2019/2/1
 * <p>
 * 1. 支持remoteTokenServices 负载均衡
 * 2. 支持 获取用户全部信息
 */
public abstract class BaseResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {
	@Autowired
	protected ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;
	@Autowired
	protected BaseAccessDeniedHandler accessDeniedHandler;
	@Autowired
	protected RemoteTokenServices remoteTokenServices;
	@Autowired
	protected UserDetailsService userDetailsService;
	@Autowired
	private FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;
	@Autowired
	private TokenStore tokenStore;
	/**
	 * 默认的配置，对外暴露
	 *
	 * @param http
	 * @throws Exception
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception{
		http.oauth2ResourceServer(config -> config.bearerTokenResolver(new HeaderBearerTokenResolver("token")).jwt());
		//允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
		http.headers().frameOptions().disable();
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
		filterIgnorePropertiesConfig.getUrls()
			.forEach(url -> registry.antMatchers(url).permitAll());
		//禁用了 CSRF（跨站请求伪造）保护。在某些情况下，禁用 CSRF 可能是必要的，但需要谨慎使用，因为它可以增加安全风险。
		registry.anyRequest().authenticated()
		.and().csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	/**
	 * 提供子类重写
	 * <p>
	 * 1. 不重写，默认支持获取userDetails
	 * 2. 重写notGetUser，提供性能
	 * <p>
	 * see codegen ResourceServerConfigurer
	 *
	 * @param resources
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		checkTokenRemoteWithUserDetails(resources);
	}

	/**
	 * 不获取用户详细 只有用户名
	 *
	 * @param resources
	 */
	protected void checkTokenRemote(ResourceServerSecurityConfigurer resources) {
		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		DefaultUserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();
		accessTokenConverter.setUserTokenConverter(userTokenConverter);

		remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
		resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint)
			.accessDeniedHandler(accessDeniedHandler)
			.tokenServices(remoteTokenServices);
	}


	/**
	 * 上下文中获取用户全部信息，两次调用userDetailsService，影响性能
	 *
	 * @param resources
	 */
	private void checkTokenRemoteWithUserDetails(ResourceServerSecurityConfigurer resources) {
		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		DefaultUserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();
		userTokenConverter.setUserDetailsService(userDetailsService);
		accessTokenConverter.setUserTokenConverter(userTokenConverter);

		remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
		resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint)
			.accessDeniedHandler(accessDeniedHandler)
			.tokenServices(remoteTokenServices);
	}

	protected void checkTokenLocal(ResourceServerSecurityConfigurer resources) {
		resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint)
			.accessDeniedHandler(accessDeniedHandler)
			.tokenStore(tokenStore);
	}
}
