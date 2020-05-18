package org.codeme.im.imclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * IMClientProjectConfig
 *
 * @author walker lee
 * @date 2020/5/15
 */
@Configuration
public class IMClientProjectConfig {
    @Value("${imclient.netty.server.port}")
    private int nettyPort;
    @Value("${imclient.netty.server.host}")
    private String nettyHost;
    @Value("${channel.id}")
    private long id;


    public int getNettyPort() {
        return nettyPort;
    }

    public String getNettyHost() {
        return nettyHost;
    }

    public long getId() {
        return id;
    }
}
