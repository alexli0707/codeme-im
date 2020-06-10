package org.codeme.im.imforward.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * IMForwardProperties
 *
 * @author walker lee
 * @date 2020/6/2
 */
@Configuration
public class IMForwardProperties {
    @Value("${codeme.im.imforward.zookeeper-url}")
    private String zookeeperUrl;

    @Value("${codeme.im.imforward.forward-id}")
    private int forwardId;


    public String getZookeeperUrl() {
        return zookeeperUrl;
    }

    public int getForwardId() {
        return forwardId;
    }
}
