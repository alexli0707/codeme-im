package org.codeme.im.imforward.netty.inner.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imcommon.model.vo.ProtocolMsg;
import org.codeme.im.imforward.config.IMForwardProperties;
import org.codeme.im.imforward.netty.inner.initializer.InnerClientInitializer;
import org.codeme.im.imforward.service.impl.InnerImReconnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
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
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InnerImClient implements InnerImClientFunction {
    //内网netty server地址
    private String innerNettyUrl;

    private String innerNettyServerId;


    @Autowired
    IMForwardProperties imForwardProperties;

    private EventLoopGroup group = new NioEventLoopGroup();

    private SocketChannel socketChannel;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private InnerImReconnectionService imReconnectionService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    public boolean start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        /**
         * NioSocketChannel用于创建客户端通道，而不是NioServerSocketChannel。
         * 请注意，我们不像在ServerBootstrap中那样使用childOption()，因为客户端SocketChannel没有父服务器。
         */
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new InnerClientInitializer(applicationContext, innerNettyServerId));
        String[] hostAndPort = innerNettyUrl.split(":");
        ChannelFuture future = bootstrap.connect(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
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
    public void reconnect() throws InterruptedException {
        if (isSocketActive()) {
            log.info("连接正常,无需重连");
            return;
        }
        log.info("正在重新连接");
        boolean rs = start();
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

    public void setInnerNettyUrl(String innerNettyUrl) {
        this.innerNettyUrl = innerNettyUrl;
    }

    private boolean isSocketActive() {
        return null != socketChannel && socketChannel.isActive();
    }

    public void setInnerNettyServerId(String innerNettyServerId) {
        this.innerNettyServerId = innerNettyServerId;
    }

    public boolean sendMsg(ProtocolMsg protocolMsg) {
        if (null != socketChannel && socketChannel.isActive()) {
            socketChannel.writeAndFlush(protocolMsg);
            return true;
        } else {
            return false;
        }
    }
}
