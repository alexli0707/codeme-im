package org.codeme.im.imcommon.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * BaseImMsg
 * 基础消息(子类,文本消息,图片,视频,语音,表情...)
 *
 * @author walker lee
 * @date 2020/5/25
 */
@Data
public class BaseImMsg {
    //客户端本地id
    protected Long localId;
    //服务端id
    protected Long serverId;

    protected Long senderId;
    protected Long receiverId;
    protected Integer msgType;
    protected Date createAt;

    public BaseImMsg() {
    }

    public BaseImMsg(Long localId, Long senderId, Long receiverId, Integer msgType, Date createAt) {
        this.localId = localId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.msgType = msgType;
        this.createAt = createAt;
    }
}
