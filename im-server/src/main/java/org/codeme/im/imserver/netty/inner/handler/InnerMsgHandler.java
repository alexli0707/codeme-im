package org.codeme.im.imserver.netty.inner.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imcommon.constant.MsgConstant;
import org.codeme.im.imcommon.constant.SocketAuthStatus;
import org.codeme.im.imcommon.model.vo.ProtocolMsg;
import org.codeme.im.imcommon.util.MsgBuilder;
import org.codeme.im.imcommon.util.SnowFlake;
import org.codeme.im.imserver.config.IMServerProjectProperties;
import org.codeme.im.imserver.util.InnerSocketHolder;
import org.codeme.im.imserver.util.OuterSocketHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * MsgHandler
 *
 * @author walker lee
 * @date 2020/5/17
 */
@Slf4j
//@ChannelHandler.Sharable
//@Component
//@Scope("prototype")
public class InnerMsgHandler extends SimpleChannelInboundHandler<ProtocolMsg> {

    private long forwardId;

    private SocketAuthStatus authStatus = SocketAuthStatus.INIT;

    //    @Autowired
    private RedisTemplate redisTemplate;

    private ApplicationContext applicationContext;

    private IMServerProjectProperties imServerProjectProperties;

    private SnowFlake serverMsgIdGenerator;

    public InnerMsgHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.redisTemplate = this.applicationContext.getBean("redisTemplate", RedisTemplate.class);
        this.serverMsgIdGenerator = this.applicationContext.getBean(SnowFlake.class);
        this.imServerProjectProperties = this.applicationContext.getBean(IMServerProjectProperties.class);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info(authStatus.toString());
        authStatus = SocketAuthStatus.WATING;
    }

    /**
     * 取消绑定
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive");
        onInactive(ctx);
    }

    private void onInactive(ChannelHandlerContext ctx) {
        authStatus = SocketAuthStatus.CLOSE;
        if (0 != this.forwardId) {
            InnerSocketHolder.remove((NioSocketChannel) ctx.channel());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("已经{}秒没有收到信息！准备关闭僵尸连接", MsgConstant.RPC_MAX_IDLE_DURATION);
                onInactive(ctx);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolMsg protocolMsg) throws Exception {
        log.debug("收到消息: " + protocolMsg.toString());
        int cmdType = protocolMsg.getCmdType();
        long receiverId = protocolMsg.getReceiverId();
        long senderId = protocolMsg.getSenderId();
        NioSocketChannel nioSocketChannel;
        switch (cmdType) {
            case MsgConstant.MsgCmdType.RPC_AUTH:
                //返回rpc auth成功消息
                ChannelFuture rpcAuthFuture = ctx.writeAndFlush(MsgBuilder.makeRPCAuthSuccessMsg(senderId));
                if (rpcAuthFuture.isSuccess()) {
                    this.forwardId = senderId;
                    InnerSocketHolder.put((NioSocketChannel) ctx.channel());
                } else {
                    log.info("发送auth success失败,关闭释放channel");
                    closeAndRemoveChannel(ctx);
                }
                break;
            case MsgConstant.MsgCmdType.PING:
                //返回pong消息
                ChannelFuture channelFuture = ctx.writeAndFlush(MsgBuilder.makePongMsg(protocolMsg.getSenderId()));
                if (!channelFuture.isSuccess()) {
                    log.info("发送pong失败,关闭释放channel");
                    closeAndRemoveChannel(ctx);
                }
                break;
            case MsgConstant.MsgCmdType.SEND_TEXT_MSG:
                nioSocketChannel = OuterSocketHolder.get(receiverId);
                if (null == nioSocketChannel) {
                    log.warn("用户 [{}] 还没有上线", receiverId);
                    break;
                }
                nioSocketChannel.writeAndFlush(protocolMsg);
                break;
            case MsgConstant.MsgCmdType.SEND_CHATROOM_TEXT_MSG:
                nioSocketChannel = OuterSocketHolder.get(receiverId);
                if (null == nioSocketChannel) {
                    log.warn("用户 [{}] 还没有上线", receiverId);
                    break;
                }
                nioSocketChannel.writeAndFlush(protocolMsg);
                break;
            default:
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("异常:", cause);
        onInactive(ctx);
        super.exceptionCaught(ctx, cause);
    }


    private void closeAndRemoveChannel(ChannelHandlerContext ctx) {
        ctx.channel().close();
        onInactive(ctx);
    }
}
