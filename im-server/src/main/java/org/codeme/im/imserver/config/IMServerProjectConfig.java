package org.codeme.im.imserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * IMServerProjectConfig
 *
 * @author walker lee
 * @date 2020/5/15
 */
@Configuration
public class IMServerProjectConfig {

    @Value("${imserver.netty.server.port}")
    private int nettyPort;


    public int getNettyPort() {
        return nettyPort;
    }
}
