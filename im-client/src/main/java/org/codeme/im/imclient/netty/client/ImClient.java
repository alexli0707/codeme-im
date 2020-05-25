package org.codeme.im.imclient.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imclient.config.IMClientProjectProperties;
import org.codeme.im.imclient.netty.initializer.ClientInitializer;
import org.codeme.im.imclient.service.impl.ImReconnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
public class ImClient {

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


    @EventListener(ApplicationReadyEvent.class)
    public boolean start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        /**
         * NioSocketChannel用于创建客户端通道，而不是NioServerSocketChannel。
         * 请注意，我们不像在ServerBootstrap中那样使用childOption()，因为客户端SocketChannel没有父服务器。
         */
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ClientInitializer(applicationContext));
        ChannelFuture future = bootstrap.connect(imClientProjectProperties.getNettyHost(), imClientProjectProperties.getNettyPort());
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

    public void reconnect() throws InterruptedException {
        if (null != socketChannel && socketChannel.isActive()) {
            log.info("连接正常,无需重连");
            return;
        }
        log.info("正在重新连接");
        boolean rs = start();
        log.info("重连结果:"+String.valueOf(rs));
        if (rs) {
            imReconnectionService.onReconnect();
        }
    }

    public void close() {
        if (null != socketChannel) {
            socketChannel.close();
        }
    }

}
