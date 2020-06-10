package org.codeme.im.imserver.netty.inner.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imserver.config.IMServerProjectProperties;
import org.codeme.im.imserver.netty.inner.initializer.InnerServerInitializer;
import org.codeme.im.imserver.service.impl.CuratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * InerRPCServer
 *
 * @author walker lee
 * @date 2020/6/8
 */
@Component
@Slf4j
public class InnerRPCServer {
    @Autowired
    private IMServerProjectProperties imServerProjectProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CuratorService curatorService;


    private EventLoopGroup acceptLoopGroup = new NioEventLoopGroup();
    private EventLoopGroup socketLoopGroup = new NioEventLoopGroup();


    /**
     * 启动 Netty
     *
     * @return
     * @throws InterruptedException
     */
    @EventListener(ApplicationReadyEvent.class)
    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(acceptLoopGroup, socketLoopGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(imServerProjectProperties.getInnerNettyPort()))
                //保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new InnerServerInitializer(applicationContext));
        //绑定并开始接受传入的连接。
        ChannelFuture future = bootstrap.bind().sync();
        if (future.isSuccess()) {
            log.info("启动rpc Netty 成功");
            //准备注册zk
            curatorService.register();
        }
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        acceptLoopGroup.shutdownGracefully().syncUninterruptibly();
        socketLoopGroup.shutdownGracefully().syncUninterruptibly();
        log.info("关闭rpc Netty 成功");
    }
}
