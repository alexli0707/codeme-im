package org.codeme.im.imcommon.model.vo;

import lombok.Data;

/**
 * ProtocolMsg
 * 消息结构:消息头 + 消息体
 * * 消息头结构(版本1):  cmd_type(4 bytes:int)| sender_id(8 bytes:long)  |receiver_id(8 bytes:long) | protocol_version:消息版本(4 bytes:int)|content_length(4 bytes)
 * * 消息头结构(版本2):  cmd_type(4 bytes:int)| sender_id(8 bytes:long)  |receiver_id(8 bytes:long) | chatroom_id(8 bytes:long)| protocol_version:消息版本(4 bytes:int)|content_length(4 bytes)
 *
 * @author walker lee
 * @date 2020/5/15
 */
@Data
public class ProtocolMsg {
    private int cmdType;
    private long senderId;
    private long receiverId;
    private long chatroomId;
    private int protocolVersion;
    private int contentLength;
    private String msgContent = "";

    public ProtocolMsg() {
    }

    public ProtocolMsg(int cmdType, long senderId, long receiverId, int protocolVersion, int contentLength, String msgContent) {
        this.cmdType = cmdType;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.protocolVersion = protocolVersion;
        this.contentLength = contentLength;
        this.msgContent = msgContent;
        this.chatroomId = 0;
    }

    public ProtocolMsg(int cmdType, long senderId, long receiverId, long chatroomId, int protocolVersion, int contentLength, String msgContent) {
        this.cmdType = cmdType;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.chatroomId = chatroomId;
        this.protocolVersion = protocolVersion;
        this.contentLength = contentLength;
        this.msgContent = msgContent;
    }
}
