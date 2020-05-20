package org.codeme.im.imclient.netty.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import org.codeme.im.imclient.netty.handler.ClientMsgHandler;
import org.codeme.im.imcommon.constant.MsgConstant;
import org.codeme.im.imcommon.netty.decoder.MsgDecoder;
import org.codeme.im.imcommon.netty.encoder.MsgEncoder;

/**
 * CustomerHandleInitializer
 *
 * @author walker lee
 * @date 2020/5/17
 */
public class ClientInitializer extends ChannelInitializer<Channel> {

//    private ApplicationContext applicationContext;
//
//    public ClientInitializer(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                //10 秒没发送消息 将IdleStateHandler 添加到 ChannelPipeline 中
                .addLast(new IdleStateHandler(0, MsgConstant.PING_GAP, 0))
                .addLast(new MsgEncoder())
                .addLast(new MsgDecoder())
                .addLast(new ClientMsgHandler());
    }
}