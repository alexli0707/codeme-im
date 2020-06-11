package org.codeme.im.imforward.netty.inner.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imcommon.constant.MsgConstant;
import org.codeme.im.imcommon.constant.SocketAuthStatus;
import org.codeme.im.imcommon.model.vo.ProtocolMsg;
import org.codeme.im.imcommon.util.MsgBuilder;
import org.codeme.im.imforward.config.IMForwardProperties;
import org.codeme.im.imforward.event.NeedStartReconnectEvent;
import org.codeme.im.imforward.service.impl.ForwardManagerService;
import org.springframework.context.ApplicationContext;

/**
 * ClientMsgHandler
 *
 * @author walker lee
 * @date 2020/5/17
 */
@Slf4j
//@Component
//@Scope("prototype")
public class InnerClientMsgHandler extends SimpleChannelInboundHandler<ProtocolMsg> {

    private SocketAuthStatus socketAuthStatus;

    //    @Autowired
    private IMForwardProperties imForwardProperties;


    private ApplicationContext applicationContext;

    private String innerNettyServerId;

    private ForwardManagerService forwardManagerService;


    //除开授权验证失败的都需要尝试重连
    private boolean needReconnect;

    public InnerClientMsgHandler(ApplicationContext applicationContext, String innerNettyServerId) {
        this.applicationContext = applicationContext;
        this.innerNettyServerId = innerNettyServerId;
        this.imForwardProperties = this.applicationContext.getBean(IMForwardProperties.class);
        this.forwardManagerService = this.applicationContext.getBean(ForwardManagerService.class);
        this.needReconnect = true;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("in channelActive");
        socketAuthStatus = SocketAuthStatus.WATING;
        ProtocolMsg rpcAuthMsg = MsgBuilder.makeRPCAuthMsg(imForwardProperties.getForwardId());
        ctx.writeAndFlush(rpcAuthMsg).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("in channelInactive");
        if (needReconnect) {
            log.info("发送需要重连事件");
            //发送需要重连事件
            applicationContext.publishEvent(new NeedStartReconnectEvent(applicationContext, innerNettyServerId));
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("start ping...");
                //向服务端发送消息
                ProtocolMsg pingMsg = MsgBuilder.makePingMsg(imForwardProperties.getForwardId());
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
        log.debug("客户端收到消息={}", protocolMsg);
        int cmdType = protocolMsg.getCmdType();
        long receiverId = protocolMsg.getReceiverId();
        switch (cmdType) {
            case MsgConstant.MsgCmdType.PONG:
                log.info("收到pong,连接正常");
                break;
            case MsgConstant.MsgCmdType.RPC_AUTH_SUCCESS:
                log.info("rpc auth success,连接正常");
                break;
            case MsgConstant.MsgCmdType.SEND_TEXT_MSG:
                log.info(String.format("收到来自{%d}的消息:%s", protocolMsg.getSenderId(), protocolMsg.getMsgContent()));
                this.forwardManagerService.forwardMsg(protocolMsg, receiverId);
                break;
            case MsgConstant.MsgCmdType.SEND_CHATROOM_TEXT_MSG:
                log.info(String.format("收到来自群{%d}的群消息:%s", protocolMsg.getReceiverId(), protocolMsg.getMsgContent()));
                break;
            case MsgConstant.MsgCmdType.ACK_TEXT_MSG:
                log.info("收到ack消息:{}", protocolMsg);
                break;
            case MsgConstant.MsgCmdType.ACK_CHATROOM_TEXT_MSG:
                log.info("收到ack群消息:{}", protocolMsg);
                break;
            default:
                log.error("未知消息");
                break;

        }
    }
}
