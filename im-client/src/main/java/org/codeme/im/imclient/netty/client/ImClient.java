package org.codeme.im.imclient.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imclient.config.IMClientProjectProperties;
import org.codeme.im.imclient.netty.initializer.ClientInitializer;
import org.codeme.im.imclient.service.impl.ApiServiceImpl;
import org.codeme.im.imclient.service.impl.ImReconnectionService;
import org.codeme.im.imcommon.constant.MsgConstant;
import org.codeme.im.imcommon.http.RestHttpResponse;
import org.codeme.im.imcommon.http.exp.RestHttpException;
import org.codeme.im.imcommon.model.vo.AccessToken;
import org.codeme.im.imcommon.model.vo.TextMsg;
import org.codeme.im.imcommon.util.MsgBuilder;
import org.codeme.im.imcommon.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Client
 *
 * @author walker lee
 * @date 2020/5/17
 */
@Component
@Slf4j
public class ImClient implements ImClientFunction {

    @Autowired
    IMClientProjectProperties imClientProjectProperties;

    private EventLoopGroup group = new NioEventLoopGroup();

    private SocketChannel socketChannel;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ImReconnectionService imReconnectionService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private SnowFlake msgIdGenerator;

    @Autowired
    ApiServiceImpl apiServiceImpl;


    @EventListener(ApplicationReadyEvent.class)
    public boolean start() throws InterruptedException, IOException, RestHttpException {
        //准备完成验证
        RestHttpResponse<AccessToken> tokenResponse = apiServiceImpl.oauth(this.imClientProjectProperties.getUserName(), this.imClientProjectProperties.getPassword());
        if (!tokenResponse.isSuccess()) {
            //验证失败
            return false;
        }
        String accessToken = tokenResponse.getData().getAccessToken();
        RestHttpResponse<String> imServerUrlResponse = apiServiceImpl.getIMServerUrl(accessToken);
        String imServerUrl = imServerUrlResponse.getData();
        String[] nettyParams = imServerUrl.split(":");
        Bootstrap bootstrap = new Bootstrap();
        /**
         * NioSocketChannel用于创建客户端通道，而不是NioServerSocketChannel。
         * 请注意，我们不像在ServerBootstrap中那样使用childOption()，因为客户端SocketChannel没有父服务器。
         */
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ClientInitializer(applicationContext));
//        ChannelFuture future = bootstrap.connect(imClientProjectProperties.getNettyHost(), imClientProjectProperties.getNettyPort());
        ChannelFuture future = bootstrap.connect(nettyParams[0], Integer.parseInt(nettyParams[1]));
        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        if (future.isSuccess()) {
            log.info("启动 Netty 成功");
            socketChannel = (SocketChannel) future.channel();
            return true;
        } else {
            log.error("连接失败");
            return false;
        }
    }

    @Override
    public void reconnect() throws InterruptedException, IOException, RestHttpException {
        if (isSocketActive()) {
            log.info("连接正常,无需重连");
            return;
        }
        log.info("正在重新连接");
        boolean rs = this.start();
        log.info("重连结果:" + String.valueOf(rs));
        if (rs) {
            imReconnectionService.onReconnect();
        }
    }

    @Override
    public void close() {
        if (null != socketChannel) {
            socketChannel.close();
        }
    }

    @Override
    public boolean sendTextMsg(long receiverId, String text) {
        if (isSocketActive() && imClientProjectProperties.getId() != 0) {
            ChannelFuture future = socketChannel.writeAndFlush(MsgBuilder.makeTextMsg(imClientProjectProperties.getId(),
                    receiverId, new TextMsg(msgIdGenerator.nextId(), imClientProjectProperties.getId(), receiverId, MsgConstant.MsgContentType.TEXT, new Date(), text)));
            future.addListener((ChannelFutureListener) channelFuture ->
                    log.info("客户端手动发消息成功={}", text));
            return true;
        } else {
            log.info("连接与授权还未结束");
            return false;
        }
    }

    @Override
    public boolean sendChatroomTextMsg(long chatroomId, String text) {
        if (isSocketActive() && imClientProjectProperties.getId() != 0) {
            ChannelFuture future = socketChannel.writeAndFlush(MsgBuilder.makeChatroomTextMsg(imClientProjectProperties.getId(),
                    chatroomId, new TextMsg(msgIdGenerator.nextId(), imClientProjectProperties.getId(), chatroomId, MsgConstant.MsgContentType.TEXT, new Date(), text)));
            future.addListener((ChannelFutureListener) channelFuture ->
                    log.info("客户端手动发群oon消息成功={}", text));
            return true;
        } else {
            log.info("连接与授权还未结束");
            return false;
        }
    }

    private boolean isSocketActive() {
        return null != socketChannel && socketChannel.isActive();
    }


}
