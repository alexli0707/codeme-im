package org.codeme.im.imforward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * IMForwardApplication
 *
 * @author walker lee
 * @date 2020/6/2
 */
@SpringBootApplication
@EnableDiscoveryClient
public class IMForwardApplication {
    public static void main(String[] args) {
        SpringApplication.run(IMForwardApplication.class, args);
    }
}
