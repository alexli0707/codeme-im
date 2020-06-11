package org.codeme.im.imserver.service.impl;

import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imserver.util.InnerSocketHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RoundRobbinForwardMsgServiceImpl
 *
 * @author walker lee
 * @date 2020/6/11
 */
@Service
@Slf4j
public class RoundRobbinForwardMsgServiceImpl {
    private AtomicInteger roundRobbinInt = new AtomicInteger(0);


    public NioSocketChannel getRoundRobbinChannel() {
        int roundRobbinCount = roundRobbinInt.incrementAndGet();
        List<NioSocketChannel> nioSocketChannels = (List<NioSocketChannel>) InnerSocketHolder.getList();
        if (nioSocketChannels.size() == 0) {
            return null;
        }
        return nioSocketChannels.get(Math.abs(roundRobbinCount % nioSocketChannels.size()));
    }


}
