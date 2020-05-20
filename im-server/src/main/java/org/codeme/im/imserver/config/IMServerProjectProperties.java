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
public class IMServerProjectProperties {

    @Value("${imserver.netty.server.port}")
    private int nettyPort;

    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${spring.redis.database}")
    private String redisDatabase;
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String redisPort;
    @Value("${spring.redis.password}")
    private String redisPassword;


    public int getNettyPort() {
        return nettyPort;
    }

    public String getActiveProfile() {
        return activeProfile;
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
