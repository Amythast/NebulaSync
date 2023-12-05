package com.nebula.governance;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 服务治理
 *
 * @author feifeixia
 * 2019/4/28 15:21
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
public class NebulaGovernanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NebulaGovernanceApplication.class, args);
    }
}
