package org.codeme.im.imcommon.constant;

/**
 * MsgConstant
 *
 * @author walker lee
 * @date 2020/5/15
 */
public class MsgConstant {
    //心跳包间隔时间(s)
    public static final int PING_GAP = 8;
    //服务端没有收到socket任何消息的最大时间
    public static final int MAX_IDLE_DURATION = 30;

    public static class Version {
        public static final int LONG_CONNECTION = 1;
    }

    public static class MsgCmdType {
        public static final int AUTH = 99;
        public static final int AUTH_SUCCESS = 100;
        public static final int AUTH_FAIL = 101;
        public static final int PING = 1;
        public static final int PONG = 2;
        public static final int SEND_TEXT_MSG = 11;
        public static final int ACK_TEXT_MSG = 12;

    }
}
