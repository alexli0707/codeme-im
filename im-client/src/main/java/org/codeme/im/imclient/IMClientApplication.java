package org.codeme.im.imclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * IMClientApplication
 *
 * @author walker lee
 * @date 2020/5/15
 */
@SpringBootApplication(scanBasePackages = {"org.codeme"})
public class IMClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(IMClientApplication.class, args);
    }
}
