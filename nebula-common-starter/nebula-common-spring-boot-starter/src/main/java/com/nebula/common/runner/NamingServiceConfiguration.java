package com.nebula.common.runner;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author feifeixia
 * For naming service
 * @date 2023/11/24-00:52
 */
@Slf4j
@Component
public class NamingServiceConfiguration {

	@Value("${spring.cloud.nacos.discovery.server-addr}")
	private String serverAddr;
	@Bean
	public NamingService namingService() throws Exception {
		Properties properties = new Properties();
		properties.put("serverAddr", serverAddr);
		NamingService namingService = NacosFactory.createNamingService(properties);

		try {
			namingService.subscribe("cbas-auth","DEFAULT_GROUP",new EventListener() {
				@Override
				public void onEvent(Event event) {
					if (event instanceof NamingEvent) {
						log.info("服务名："+((NamingEvent)event).getServiceName());
						log.info("实例："+((NamingEvent)event).getInstances());
					}
				}
			});
		} catch (NacosException e) {
			throw new RuntimeException(e);
		}
		return namingService;
	}
}
