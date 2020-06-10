package org.codeme.im.imforward.task.runnable;

import org.codeme.im.imforward.service.impl.ForwardManagerService;
import org.springframework.context.ApplicationContext;

/**
 * ReconnectWorker
 * 重连工作者
 *
 * @author walker lee
 * @date 2020/5/25
 */
public class InnerReconnectTask implements Runnable {

    private ApplicationContext applicationContext;

    private String innerNettyServerId;

    public InnerReconnectTask(ApplicationContext applicationContext, String innerNettyServerId) {
        this.applicationContext = applicationContext;
        this.innerNettyServerId = innerNettyServerId;
    }

    @Override
    public void run() {
        ForwardManagerService forwardManagerService = this.applicationContext.getBean(ForwardManagerService.class);
        try {
            forwardManagerService.reconnectByServerId(this.innerNettyServerId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
