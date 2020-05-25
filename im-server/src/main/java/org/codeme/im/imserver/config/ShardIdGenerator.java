package org.codeme.im.imserver.config;

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
    public SnowFlake msgIdGenerator(IMServerProjectProperties imServerProjectProperties) {
        return new SnowFlake(imServerProjectProperties.getDataCenterId(), imServerProjectProperties.getMachineId());
    }


}
