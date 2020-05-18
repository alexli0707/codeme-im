package org.codeme.im.imcommon.constant;

/**
 * MsgConstant
 *
 * @author walker lee
 * @date 2020/5/15
 */
public class MsgConstant {
    //心跳包间隔时间(s)
    public static final int PING_GAP = 5;
    //服务端没有收到socket任何消息的最大时间
    public static final int MAX_IDLE_DURATION = 10;

    public static class Version {
        public static final int LONG_CONNECTION = 1;
    }

    public static class MsgCmdType {
        public static final int AUTH = 99;
        public static final int PING = 1;
        public static final int PONG = 2;
        public static final int SEND_MSG = 11;
    }
}
