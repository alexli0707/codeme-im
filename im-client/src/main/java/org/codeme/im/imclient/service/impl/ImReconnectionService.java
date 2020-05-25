package org.codeme.im.imclient.service.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imclient.task.runnable.ReconnectTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * ImReconnectionService
 *
 * @author walker lee
 * @date 2020/5/25
 */
@Service
@Slf4j
public class ImReconnectionService {

    private ScheduledExecutorService scheduledExecutorService;

    @Autowired
    ApplicationContext applicationContext;

    public void start() {
        ensureExcutor();
        scheduledExecutorService.scheduleAtFixedRate(new ReconnectTask(applicationContext), 0, 8, TimeUnit.SECONDS);
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
