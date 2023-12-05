package com.nebula.rbac.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author feifeixia
 * @date 2017年10月27日13:59:05
 */
@EnableAsync
@SpringBootApplication
@EnableDiscoveryClient
public class NebulaRbacApplication {
    public static void main(String[] args) {
        SpringApplication.run(NebulaRbacApplication.class, args);
    }
}
