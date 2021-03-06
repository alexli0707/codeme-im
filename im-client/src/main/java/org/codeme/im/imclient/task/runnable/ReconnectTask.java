package org.codeme.im.imclient.task.runnable;

import org.codeme.im.imclient.netty.client.ImClient;
import org.codeme.im.imcommon.http.exp.RestHttpException;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * ReconnectWorker
 * 重连工作者
 *
 * @author walker lee
 * @date 2020/5/25
 */
public class ReconnectTask implements Runnable {

    private ApplicationContext applicationContext;

    public ReconnectTask(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {
        ImClient imClient = this.applicationContext.getBean(ImClient.class);
        try {
            imClient.reconnect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RestHttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
