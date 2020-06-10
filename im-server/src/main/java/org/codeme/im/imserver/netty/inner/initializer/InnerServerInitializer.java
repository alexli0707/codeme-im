package org.codeme.im.imserver.netty.inner.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import org.codeme.im.imcommon.constant.MsgConstant;
import org.codeme.im.imcommon.netty.decoder.MsgDecoder;
import org.codeme.im.imcommon.netty.encoder.MsgEncoder;
import org.codeme.im.imserver.netty.inner.handler.InnerMsgHandler;
import org.springframework.context.ApplicationContext;

/**
 * HeartbeatInitializer
 *
 * @author walker lee
 * @date 2020/5/17
 */
public class InnerServerInitializer extends ChannelInitializer<Channel> {

    private ApplicationContext applicationContext;

    public InnerServerInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                .addLast(new IdleStateHandler(MsgConstant.RPC_MAX_IDLE_DURATION, 0, 0))
                .addLast(new MsgDecoder())
                .addLast(new MsgEncoder())
                .addLast(new InnerMsgHandler(applicationContext));
    }
}
