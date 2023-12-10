package com.nebula.common.security.config;

import com.nebula.common.security.component.OAuth2RestTemplateWithScope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.security.oauth2.client.AccessTokenContextRelay;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.OAuth2ClientConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.*;

/**
 * @author feifeixia
 * @date 2019/2/1
 * 注入AccessTokenContextRelay 解决feign 传递token 为空问题
 */
@Configuration
@ConditionalOnWebApplication
public class NebulaSecurityAutoConfiguration {

	@Bean
	public AccessTokenContextRelay accessTokenContextRelay(
			@Qualifier("oauth2ClientContext")OAuth2ClientContext context
	) {
		return new AccessTokenContextRelay(context);
	}

	@Bean
	public OAuth2RestTemplateWithScope oAuth2RestTemplateWithScope(
			@Qualifier("lbRestTemplate") RestTemplate restTemplate
	){
		return new OAuth2RestTemplateWithScope(restTemplate);
	}

	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Conditional(OAuth2OnClientInResourceServerCondition.class)
	@interface ConditionalOnOAuth2ClientInResourceServer {

	}

	private static class OAuth2OnClientInResourceServerCondition
		extends AllNestedConditions {

		public OAuth2OnClientInResourceServerCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnBean(ResourceServerConfiguration.class)
		static class Server {
		}

		@ConditionalOnBean(OAuth2ClientConfiguration.class)
		static class Client {
		}

	}
}
