package org.codeme.im.imserver.netty.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imcommon.constant.MsgConstant;
import org.codeme.im.imcommon.constant.RedisKeyConstant;
import org.codeme.im.imcommon.constant.ServerStatusCode;
import org.codeme.im.imcommon.constant.SocketAuthStatus;
import org.codeme.im.imcommon.model.vo.ProtocolMsg;
import org.codeme.im.imcommon.util.MsgBuilder;
import org.codeme.im.imserver.util.NettySocketHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

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
public class MsgHandler extends SimpleChannelInboundHandler<ProtocolMsg> {

    private long userId;

    private SocketAuthStatus authStatus = SocketAuthStatus.INIT;

    //    @Autowired
    private RedisTemplate redisTemplate;

    private ApplicationContext applicationContext;

    public MsgHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info(authStatus.toString());
        this.redisTemplate = this.applicationContext.getBean("redisTemplate", RedisTemplate.class);
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
        NettySocketHolder.remove((NioSocketChannel) ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("已经60秒没有收到信息！准备关闭僵尸连接");
                NettySocketHolder.remove((NioSocketChannel) ctx.channel());
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolMsg protocolMsg) throws Exception {
        log.debug("收到消息: " + protocolMsg.toString());
        NettySocketHolder.put(protocolMsg.getSenderId(), (NioSocketChannel) ctx.channel());
        int cmdType = protocolMsg.getCmdType();
        switch (cmdType) {
            case MsgConstant.MsgCmdType.AUTH:
                boolean authSuccess = false;
                //验证授权
                if (this.isAuthSuccess()) {
                    //已经授权成功的
                    ctx.writeAndFlush(MsgBuilder.makeAuthSuccessMsg(userId));
                    authSuccess = true;
                } else {
                    String accessToken = protocolMsg.getMsgContent();
                    if (StringUtils.isEmpty(accessToken)) {

                    } else {
                        String id = (String) redisTemplate.opsForValue().get(RedisKeyConstant.getAccessTokenKey(accessToken));
                        if (null == id) {
                        } else {
                            this.userId = Long.parseLong(id);
                            authSuccess = true;
                        }
                    }
                }
                if (authSuccess) {
                    ChannelFuture channelFuture = ctx.writeAndFlush(MsgBuilder.makeAuthSuccessMsg(userId));
                    if (channelFuture.isSuccess()) {
                        NettySocketHolder.put(userId, (NioSocketChannel) ctx.channel());
                        authStatus = SocketAuthStatus.SUCCESS;
                    }
                } else {
                    ctx.writeAndFlush(MsgBuilder.makeAuthFailMsg(ServerStatusCode.ERROR_TOKEN));
                    closeAndRemoveChannel(ctx);
                }
                break;
            case MsgConstant.MsgCmdType.PING:
                if (!isAuthSuccess()) {
                    closeAndRemoveChannel(ctx);
                }
                //返回pong消息
                ChannelFuture channelFuture = ctx.writeAndFlush(MsgBuilder.makePongMsg(protocolMsg.getSenderId()));
                if (!channelFuture.isSuccess()) {
                    log.info("发送pong失败,关闭释放channel");
                    closeAndRemoveChannel(ctx);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("异常:", cause);
        NettySocketHolder.remove(userId, (NioSocketChannel) ctx.channel());
        super.exceptionCaught(ctx, cause);
    }


    private boolean isAuthSuccess() {
        return authStatus.equals(SocketAuthStatus.SUCCESS) && userId != 0;
    }

    private void closeAndRemoveChannel(ChannelHandlerContext ctx) {
        ctx.channel().close();
        NettySocketHolder.remove(userId, (NioSocketChannel) ctx.channel());

    }
}
