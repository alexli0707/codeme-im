package org.codeme.im.imclient.netty.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imclient.config.IMClientProjectProperties;
import org.codeme.im.imclient.service.impl.ApiServiceImpl;
import org.codeme.im.imcommon.constant.MsgConstant;
import org.codeme.im.imcommon.constant.SocketAuthStatus;
import org.codeme.im.imcommon.http.RestHttpResponse;
import org.codeme.im.imcommon.model.vo.AccessToken;
import org.codeme.im.imcommon.model.vo.ProtocolMsg;
import org.codeme.im.imcommon.util.MsgBuilder;
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
public class ClientMsgHandler extends SimpleChannelInboundHandler<ProtocolMsg> {

    private SocketAuthStatus socketAuthStatus;

    //    @Autowired
    private IMClientProjectProperties imClientProjectProperties;

    private ApiServiceImpl apiServiceImpl;

    private ApplicationContext applicationContext;

    private long id;

    public ClientMsgHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.imClientProjectProperties = this.applicationContext.getBean(IMClientProjectProperties.class);
        this.apiServiceImpl = this.applicationContext.getBean(ApiServiceImpl.class);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("in channelActive");
        socketAuthStatus = SocketAuthStatus.WATING;
        //准备完成验证
        RestHttpResponse<AccessToken> tokenResponse = apiServiceImpl.oauth(this.imClientProjectProperties.getUserName(), this.imClientProjectProperties.getPassword());
        if (tokenResponse.isSuccess()) {
            String accessToken = tokenResponse.getData().getAccessToken();
            ctx.writeAndFlush(MsgBuilder.makeAuthMsg(accessToken)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } else {
            //验证失败,关闭连接
            log.error(tokenResponse.getMeta().getMessage());
            ctx.channel().close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("in channelInactive");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("start ping...");
                //向服务端发送消息
                ProtocolMsg pingMsg = MsgBuilder.makePingMsg(id);
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
            case MsgConstant.MsgCmdType.AUTH_SUCCESS:
                this.id = Long.parseLong(protocolMsg.getMsgContent());
                log.info("channel id是:" + protocolMsg.getMsgContent());
                break;
            case MsgConstant.MsgCmdType.AUTH_FAIL:
                channelHandlerContext.channel().close();
                break;
        }
    }
}
