package org.codeme.im.imclient.config;

import org.codeme.im.imcommon.util.SnowFlake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ShardIdGenerator
 *
 * @author walker lee
 * @date 2019-12-02
 */
@Configuration
public class ShardIdGenerator {

    @Bean
    public SnowFlake msgIdGenerator(IMClientProjectProperties imClientProjectProperties) {
        return new SnowFlake(imClientProjectProperties.getDataCenterId(), imClientProjectProperties.getMachineId());
    }


}
