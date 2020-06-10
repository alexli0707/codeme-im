package org.codeme.im.imforward.service.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * ImReconnectionService
 *
 * @author walker lee
 * @date 2020/5/25
 */
@Service
@Slf4j
public class InnerImReconnectionService {

    private ScheduledExecutorService scheduledExecutorService;

    @Autowired
    ApplicationContext applicationContext;

    public void start() {
        ensureExcutor();
//        scheduledExecutorService.scheduleAtFixedRate(new InnerReconnectTask(applicationContext), 0, 8, TimeUnit.SECONDS);
    }

    public void onReconnect() {
        scheduledExecutorService.shutdown();
    }


    private ScheduledExecutorService ensureExcutor() {
        if (null == scheduledExecutorService || scheduledExecutorService.isShutdown()) {
            ThreadFactory threadFactory = new ThreadFactoryBuilder().
                    setDaemon(true).setNameFormat("ImReconnectionService-%d").build();
            scheduledExecutorService = new ScheduledThreadPoolExecutor(1, threadFactory);
        }
        return scheduledExecutorService;
    }

}
