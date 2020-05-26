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
public class IMClientProjectProperties {
    @Value("${imclient.netty.server.port}")
    private int nettyPort;
    @Value("${imclient.netty.server.host}")
    private String nettyHost;
    @Value("${imclient.machineId}")
    private Integer machineId;
    @Value("${imclient.datacenterId}")
    private Integer dataCenterId;

    private long id;

//    private String accessToken;

    @Value("${imclient.user.username}")
    private String userName;
    @Value("${imclient.user.password}")
    private String password;
    @Value("${imclient.api-server.url}")
    private String apiServerUrl;


    public int getNettyPort() {
        return nettyPort;
    }

    public String getNettyHost() {
        return nettyHost;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApiServerUrl() {
        return apiServerUrl;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public Integer getDataCenterId() {
        return dataCenterId;
    }
}
