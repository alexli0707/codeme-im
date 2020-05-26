package org.codeme.im.imclient.netty.client;

/**
 * ImClientFunction
 * 定义客户端Im通用功能(命令)
 *
 * @author walker lee
 * @date 2020/5/26
 */
public interface ImClientFunction {
    /**
     * 重连
     */
    void reconnect() throws InterruptedException;

    /**
     * 关闭socket
     */
    void close();

    /**
     * 发送单聊文本消息
     *
     * @param receiverId
     * @param text
     */
    boolean sendTextMsg(long receiverId, String text);

    /**
     * 发送群文本消息
     *
     * @param chatroomId
     * @param text
     */
    boolean sendChatroomTextMsg(long chatroomId, String text);
}
