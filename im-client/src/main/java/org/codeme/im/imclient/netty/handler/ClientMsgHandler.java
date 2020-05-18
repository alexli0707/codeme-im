package org.codeme.im.imclient.netty.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imclient.config.IMClientProjectConfig;
import org.codeme.im.imclient.util.SpringBeanFactory;
import org.codeme.im.imcommon.constant.MsgConstant;
import org.codeme.im.imcommon.model.vo.ProtocolMsg;
import org.codeme.im.imcommon.util.MsgBuilder;

/**
 * ClientMsgHandler
 *
 * @author walker lee
 * @date 2020/5/17
 */
@Slf4j
public class ClientMsgHandler extends SimpleChannelInboundHandler<ProtocolMsg> {


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("start ping...");
                //向服务端发送消息
                ProtocolMsg pingMsg = MsgBuilder.makePingMsg(SpringBeanFactory.getBean(IMClientProjectConfig.class).getId());
                ctx.writeAndFlush(pingMsg).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }

        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 每当从服务端接收到新数据时，都会使用收到的消息调用此方法 channelRead0(),在此示例中，接收消息的类型是ProtocolMsg
     *
     * @param channelHandlerContext
     * @param protocolMsg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ProtocolMsg protocolMsg) throws Exception {
        //从服务端收到消息时被调用
        log.info("客户端收到消息={}", protocolMsg);
        int cmdType = protocolMsg.getCmdType();
        switch (cmdType) {
            case MsgConstant.MsgCmdType.PONG:
                log.info("收到pong,连接正常");
                break;
        }
    }
}
