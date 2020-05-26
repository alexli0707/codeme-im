package org.codeme.im.imclient.task.runnable;

import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imclient.netty.client.ImClient;
import org.springframework.context.ApplicationContext;

import java.util.Scanner;

/**
 * ConsoleScannerTask
 * 监听获取命令行输入指令,群聊功能之后,调整获取的格式为
 * 单聊 {接收者id}<{消息内容}
 * 群聊 {群id}>{消息内容}
 *
 * @author walker lee
 * @date 2020/5/25
 */
@Slf4j
public class ConsoleScannerTask implements Runnable {
    private static final String COMMAND_DIVIDER = ":";
    private static final String P2P_MSG_DIVIDER = "<";
    private static final String CHATROOM_MSG_DIVIDER = ">";

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
            if (msg.contains(P2P_MSG_DIVIDER)) {
                String[] msgs = msg.split(P2P_MSG_DIVIDER);
                if (msgs.length != 2) {
                    log.warn("正确的发送格式为: {接收者id}<{消息内容}");
                    continue;
                }
                imClient.sendTextMsg(Long.parseLong(msgs[0]), msgs[1]);
            } else if (msg.contains(CHATROOM_MSG_DIVIDER)) {
                String[] msgs = msg.split(CHATROOM_MSG_DIVIDER);
                if (msgs.length != 2) {
                    log.warn("正确的发送格式为: {群id}>{消息内容}");
                    continue;
                }
                imClient.sendChatroomTextMsg(Long.parseLong(msgs[0]), msgs[1]);
            }

        }

    }
}
