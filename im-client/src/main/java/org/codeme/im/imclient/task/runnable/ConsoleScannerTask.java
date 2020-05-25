package org.codeme.im.imclient.task.runnable;

import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imclient.netty.client.ImClient;
import org.springframework.context.ApplicationContext;

import java.util.Scanner;

/**
 * ConsoleScannerTask
 *
 * @author walker lee
 * @date 2020/5/25
 */
@Slf4j
public class ConsoleScannerTask implements Runnable {
    private static final String RECEIVER_DIVIDER = ":";

    private ApplicationContext applicationContext;

    private ImClient imClient;

    public ConsoleScannerTask(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {
        this.imClient = applicationContext.getBean(ImClient.class);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String msg = scanner.nextLine();
            String[] msgs = msg.split(RECEIVER_DIVIDER);
            if (msgs.length != 2) {
                log.warn("正确的发送格式为: {接收者id}|{消息内容}");
                continue;
            }
            imClient.sendTextMsg(Long.parseLong(msgs[0]), msgs[1]);
        }

    }
}
