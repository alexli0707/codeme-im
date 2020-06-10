package org.codeme.im.imforward.netty.inner.client;

/**
 * ImClientFunction
 * 定义客户端Im通用功能(命令)
 *
 * @author walker lee
 * @date 2020/5/26
 */
public interface InnerImClientFunction {
    /**
     * 重连
     */
    void reconnect() throws InterruptedException;

    /**
     * 关闭socket
     */
    void close();

}
