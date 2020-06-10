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
    @Value("${imserver.netty.server.inner-port}")
    private int innerNettyPort;
    @Value("${imserver.machineId}")
    private Integer machineId;
    @Value("${imserver.datacenterId}")
    private Integer dataCenterId;

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
    @Value("${imserver.netty.server.url}")
    private String imServerUrl;
    @Value("${imserver.netty.server.inner-url}")
    private String imInnerServerUrl;
    @Value("${imserver.zookeeper-url}")
    private String zookeeperUrl;


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

    public Integer getMachineId() {
        return machineId;
    }

    public Integer getDataCenterId() {
        return dataCenterId;
    }

    public String getZkId() {
        return String.format("%d-%d", machineId, dataCenterId);
    }

    public String getImServerUrl() {
        return imServerUrl;
    }

    public String getZookeeperUrl() {
        return zookeeperUrl;
    }

    public int getInnerNettyPort() {
        return innerNettyPort;
    }

    public String getImInnerServerUrl() {
        return imInnerServerUrl;
    }
}
