package org.codeme.im.imserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * IMServerApplication
 *
 * @author walker lee
 * @date 2020/5/15
 */
@SpringBootApplication(scanBasePackages = {"org.codeme"})
@EnableDiscoveryClient
public class IMServerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(IMServerApplication.class, args);
    }
}
