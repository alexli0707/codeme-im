package org.codeme.im.imserver.util;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * NettySocketHolder
 *
 * @author walker lee
 * @date 2020/5/17
 */
public class InnerSocketHolder {


    private static final CopyOnWriteArrayList<NioSocketChannel> INNER_SOCKET_LISTS = new CopyOnWriteArrayList<>();

    public static void put(NioSocketChannel socketChannel) {
        INNER_SOCKET_LISTS.add(socketChannel);
    }

    public static CopyOnWriteArrayList<NioSocketChannel> getList() {
        return INNER_SOCKET_LISTS;
    }


    public static void remove(NioSocketChannel nioSocketChannel) {
        INNER_SOCKET_LISTS.stream().filter(node -> node == nioSocketChannel).forEach(node -> INNER_SOCKET_LISTS.remove(node));
    }

}
