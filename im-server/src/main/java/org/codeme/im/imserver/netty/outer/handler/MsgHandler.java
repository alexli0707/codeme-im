package org.codeme.im.imserver.netty.outer.handler;

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
import org.codeme.im.imcommon.http.util.JsonTools;
import org.codeme.im.imcommon.model.vo.ProtocolMsg;
import org.codeme.im.imcommon.model.vo.TextMsg;
import org.codeme.im.imcommon.util.MsgBuilder;
import org.codeme.im.imcommon.util.SnowFlake;
import org.codeme.im.imserver.config.IMServerProjectProperties;
import org.codeme.im.imserver.constant.IMServerConstant;
import org.codeme.im.imserver.util.NettySocketHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.Set;

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
    private RedisTemplate<String, String> redisTemplate;

    private ApplicationContext applicationContext;

    private IMServerProjectProperties imServerProjectProperties;

    private SnowFlake serverMsgIdGenerator;

    public MsgHandler(ApplicationContext applicationContext) {
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
        NettySocketHolder.remove((NioSocketChannel) ctx.channel());
        log.info(String.valueOf(this.userId));
        if (0 != this.userId) {
            redisTemplate.opsForHash().delete(RedisKeyConstant.getUserSocketBelong(), String.valueOf(userId));
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("已经60秒没有收到信息！准备关闭僵尸连接");
                onInactive(ctx);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolMsg protocolMsg) throws Exception {
        log.debug("收到消息: " + protocolMsg.toString());
//        NettySocketHolder.put(protocolMsg.getSenderId(), (NioSocketChannel) ctx.channel());
        int cmdType = protocolMsg.getCmdType();
        long receiverId = protocolMsg.getReceiverId();
        long senderId = protocolMsg.getSenderId();
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
                        ctx.channel().attr(IMServerConstant.KEY_USER_ID).set(senderId);
                        redisTemplate.opsForHash().put(RedisKeyConstant.getUserSocketBelong(), String.valueOf(userId), imServerProjectProperties.getZkId());
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
            case MsgConstant.MsgCmdType.SEND_TEXT_MSG:
                if (!isAuthSuccess()) {
                    closeAndRemoveChannel(ctx);
                }
                TextMsg textMsg = JsonTools.strToObject(protocolMsg.getMsgContent(), TextMsg.class);
                textMsg.setServerId(serverMsgIdGenerator.nextId());
                protocolMsg.setMsgContent(JsonTools.simpleObjToStr(textMsg));
                protocolMsg.setContentLength(protocolMsg.getMsgContent().getBytes().length);
                ctx.writeAndFlush(MsgBuilder.makeServerAckTextMsg(protocolMsg.getSenderId(), protocolMsg.getReceiverId(), textMsg)).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("ack-{} - 文本消息成功", textMsg.getLocalId());
                    } else {
                        log.warn("ack-{}- 文本消息失败", textMsg.getLocalId());
                    }
                });
                //开始直接发送或者投递到转发服务模块
                String zkId = this.getReceiverSocketServer(receiverId);
                if (StringUtils.isEmpty(zkId)) {
                    //用户不在线
                    log.info(" id: {} 接收者不在线", receiverId);
                    break;
                } else if (zkId.equals(imServerProjectProperties.getZkId())) {
                    NioSocketChannel nioSocketChannel = NettySocketHolder.get(receiverId);
                    if (null == nioSocketChannel) {
                        log.warn("用户 [{}] 还没有上线", receiverId);
                        break;
                    }
                    nioSocketChannel.writeAndFlush(protocolMsg);
                } else {
                    //TODO 往转发服务投递
                }

                break;
            case MsgConstant.MsgCmdType.SEND_CHATROOM_TEXT_MSG:
                if (!isAuthSuccess()) {
                    closeAndRemoveChannel(ctx);
                }
                Set<String> memberSet = redisTemplate.opsForSet().members(RedisKeyConstant.getChatroomMembers(receiverId));
                memberSet.remove(senderId);
                TextMsg chatroomTextMsg = JsonTools.strToObject(protocolMsg.getMsgContent(), TextMsg.class);
                chatroomTextMsg.setServerId(serverMsgIdGenerator.nextId());
                protocolMsg.setMsgContent(JsonTools.simpleObjToStr(chatroomTextMsg));
                protocolMsg.setContentLength(protocolMsg.getMsgContent().getBytes().length);
                ctx.writeAndFlush(MsgBuilder.makeChatroomAckTextMsg(senderId, protocolMsg.getReceiverId(), chatroomTextMsg)).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("ack-{} - 群文本消息成功", chatroomTextMsg.getLocalId());
                    } else {
                        log.warn("ack-{}- 群文本消息失败", chatroomTextMsg.getLocalId());
                    }
                });
                for (String memberId :
                        memberSet) {
                    NioSocketChannel socketChannel = NettySocketHolder.get(Long.valueOf(memberId));
                    if (null == socketChannel) {
                        continue;
                    }
                    socketChannel.writeAndFlush(protocolMsg);
                }
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


    private boolean isAuthSuccess() {
        return authStatus.equals(SocketAuthStatus.SUCCESS) && userId != 0;
    }

    private void closeAndRemoveChannel(ChannelHandlerContext ctx) {
        ctx.channel().close();
        onInactive(ctx);
    }

    /**
     * 获取接收者长连接服务实例名,返回为空则用户不在线或者没有建立连接
     *
     * @param receiverId
     * @return
     */
    private String getReceiverSocketServer(long receiverId) {
        return String.valueOf(redisTemplate.opsForHash().get(RedisKeyConstant.getUserSocketBelong(), String.valueOf(receiverId)));
    }

}
