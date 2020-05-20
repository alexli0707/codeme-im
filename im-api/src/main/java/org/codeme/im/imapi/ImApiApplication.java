package org.codeme.im.imapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ImApiApplication
 *
 * @author walker lee
 * @date 2020/5/20
 */
@SpringBootApplication(scanBasePackages = {"org.codeme"})
public class ImApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImApiApplication.class, args);
    }
}
