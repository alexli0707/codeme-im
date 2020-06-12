package org.codeme.im.imcommon.constant;

/**
 * MsgConstant
 *
 * @author walker lee
 * @date 2020/5/15
 */
public class MsgConstant {
    //心跳包间隔时间(s)
    public static final int PING_GAP = 15;

    public static final int RPC_PING_GAP = 5;
    //服务端没有收到socket任何消息的最大时间
    public static final int MAX_IDLE_DURATION = 30;

    public static final int RPC_MAX_IDLE_DURATION = 9;

    public static class Version {
        public static final int LONG_CONNECTION = 1;
        public static final int CHATROOM = 2;
    }

    public static class MsgCmdType {
        public static final int RPC_AUTH = 599;
        public static final int RPC_AUTH_SUCCESS = 600;
        public static final int AUTH = 99;
        public static final int AUTH_SUCCESS = 100;
        public static final int AUTH_FAIL = 101;
        public static final int PING = 1;
        public static final int PONG = 2;
        public static final int SEND_TEXT_MSG = 11;
        public static final int ACK_TEXT_MSG = 12;
        public static final int SEND_CHATROOM_TEXT_MSG = 51;
        public static final int ACK_CHATROOM_TEXT_MSG = 52;


    }


    public static class MsgContentType {
        public static final int TEXT = 1;
    }
}
