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

    @Value("${spring.redis.database}")
    private String redisDatabase;
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String redisPort;
    @Value("${spring.redis.password}")
    private String redisPassword;


    public String getZookeeperUrl() {
        return zookeeperUrl;
    }

    public int getForwardId() {
        return forwardId;
    }

    public String getRedisDatabase() {
        return redisDatabase;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public String getRedisPort() {
        return redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }
}
