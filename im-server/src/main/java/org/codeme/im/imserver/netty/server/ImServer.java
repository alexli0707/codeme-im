package org.codeme.im.imserver.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imserver.config.IMServerProjectProperties;
import org.codeme.im.imserver.netty.initializer.ServerInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * Server
 *
 * @author walker lee
 * @date 2020/5/17
 */
@Component
@Slf4j
public class ImServer {
    @Autowired
    private IMServerProjectProperties imServerProjectProperties;


    private EventLoopGroup acceptLoopGroup = new NioEventLoopGroup();
    private EventLoopGroup socketLoopGroup = new NioEventLoopGroup();


    /**
     * 启动 Netty
     *
     * @return
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(acceptLoopGroup, socketLoopGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(imServerProjectProperties.getNettyPort()))
                //保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ServerInitializer());
        //绑定并开始接受传入的连接。
        ChannelFuture future = bootstrap.bind().sync();
        if (future.isSuccess()) {
            log.info("启动 Netty 成功");
        }
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        acceptLoopGroup.shutdownGracefully().syncUninterruptibly();
        socketLoopGroup.shutdownGracefully().syncUninterruptibly();
        log.info("关闭 Netty 成功");
    }
}
