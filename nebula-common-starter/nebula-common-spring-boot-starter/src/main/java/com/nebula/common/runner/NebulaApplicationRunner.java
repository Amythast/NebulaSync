package com.nebula.common.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @author feifeixia
 * For execution after service set up
 * @date 2023/11/24-00:21
 */
@Slf4j
@Configuration
public class NebulaApplicationRunner implements ApplicationRunner {
	private volatile boolean applicationStarted = false;


	@Override
	public void run(ApplicationArguments args) {
		log.info("Application started...");
		applicationStarted = true;
	}
}
