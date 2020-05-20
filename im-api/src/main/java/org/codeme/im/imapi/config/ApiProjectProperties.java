package org.codeme.im.imapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * ApiProjectProperties
 *
 * @author walker lee
 * @date 2019-10-09
 */
@Configuration
public class ApiProjectProperties {


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
    @Value("${codeme.password.aes}")
    private boolean isApiPasswordAesTrans;





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

    public boolean isApiPasswordAesTrans() {
        return isApiPasswordAesTrans;
    }
}
