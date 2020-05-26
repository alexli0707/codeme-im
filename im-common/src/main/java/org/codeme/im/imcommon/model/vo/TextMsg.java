package org.codeme.im.imcommon.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * TextMsg
 *
 * @author walker lee
 * @date 2020/5/25
 */
@Data
public class TextMsg extends BaseImMsg {
    private String text;

    public TextMsg() {
    }

    public TextMsg(Long localId, Long senderId, Long receiverId, Integer msgType, Date createAt, String text) {
        this.localId = localId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.msgType = msgType;
        this.createAt = createAt;
        this.text = text;
    }


}
