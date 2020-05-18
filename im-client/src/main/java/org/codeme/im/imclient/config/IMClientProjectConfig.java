package org.codeme.im.imclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * ClientProjectConfig
 *
 * @author walker lee
 * @date 2020/5/15
 */
@Configuration
public class IMClientProjectConfig {
    @Value("${netty.server.port}")
    private int nettyPort;
    @Value("${netty.server.host}")
    private String host;
    @Value("${channel.id}")
    private long id;


    public int getNettyPort() {
        return nettyPort;
    }

    public String getHost() {
        return host;
    }

    public long getId() {
        return id;
    }
}
