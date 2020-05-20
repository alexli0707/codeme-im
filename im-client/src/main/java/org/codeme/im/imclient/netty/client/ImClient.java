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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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



    @PostConstruct
    public void start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        /**
         * NioSocketChannel用于创建客户端通道，而不是NioServerSocketChannel。
         * 请注意，我们不像在ServerBootstrap中那样使用childOption()，因为客户端SocketChannel没有父服务器。
         */
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ClientInitializer());
        /**
         * 启动客户端
         * 我们应该调用connect()方法而不是bind()方法。
         */
        ChannelFuture future = bootstrap.connect(imClientProjectProperties.getNettyHost(), imClientProjectProperties.getNettyPort()).sync();
        if (future.isSuccess()) {
            log.info("启动 Netty 成功");
        } else {
            log.error("连接失败");
        }
        socketChannel = (SocketChannel) future.channel();
    }

}
