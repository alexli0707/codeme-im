package org.codeme.im.imforward.netty.inner.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.codeme.im.imcommon.constant.MsgConstant;
import org.codeme.im.imcommon.netty.decoder.MsgDecoder;
import org.codeme.im.imcommon.netty.encoder.MsgEncoder;
import org.codeme.im.imforward.netty.inner.handler.InnerClientMsgHandler;
import org.springframework.context.ApplicationContext;

/**
 * CustomerHandleInitializer
 *
 * @author walker lee
 * @date 2020/5/17
 */
public class InnerClientInitializer extends ChannelInitializer<Channel> {

    private ApplicationContext applicationContext;
    private String innerNettyServerId;

    public InnerClientInitializer(ApplicationContext applicationContext, String innerNettyServerId) {
        this.applicationContext = applicationContext;
        this.innerNettyServerId = innerNettyServerId;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                //10 秒没发送消息 将IdleStateHandler 添加到 ChannelPipeline 中
                .addLast(new IdleStateHandler(0, MsgConstant.RPC_PING_GAP, 0))
                .addLast(new LengthFieldBasedFrameDecoder(2048, 0, 2, 0, 2))
                .addLast(new LengthFieldPrepender(2))
                .addLast(new MsgEncoder())
                .addLast(new MsgDecoder())
                .addLast(new InnerClientMsgHandler(this.applicationContext,this.innerNettyServerId));
    }
}
