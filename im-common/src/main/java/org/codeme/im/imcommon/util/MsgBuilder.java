package org.codeme.im.imcommon.util;

import org.codeme.im.imcommon.constant.MsgConstant;
import org.codeme.im.imcommon.model.vo.ProtocolMsg;

/**
 * MsgBuilder
 *
 * @author walker lee
 * @date 2020/5/17
 */
public class MsgBuilder {

    public static ProtocolMsg makeMsg(int cmdType, long senderId, long receiverId, int protocolVersion, int contentLength, String msgContent) {
        ProtocolMsg protocolMsg;
        switch (protocolVersion) {
            case MsgConstant.Version.LONG_CONNECTION:
                protocolMsg = new ProtocolMsg(cmdType, senderId, receiverId, protocolVersion, contentLength, msgContent);
                break;
            default:
                throw new IllegalArgumentException("非法消息版本");
        }
        return protocolMsg;
    }

    /**
     * 生成ping消息, receiverId不关注默认为0
     *
     * @param senderId
     * @return
     */
    public static ProtocolMsg makePingMsg(long senderId) {
        return MsgBuilder.makeMsg(MsgConstant.MsgCmdType.PING, senderId, 0, MsgConstant.Version.LONG_CONNECTION, 0, "");
    }

    /**
     * 生成pong消息, senderId不关注默认为0
     *
     * @param receiverId
     * @return
     */
    public static ProtocolMsg makePongMsg(long receiverId) {
        return MsgBuilder.makeMsg(MsgConstant.MsgCmdType.PONG, 0, receiverId, MsgConstant.Version.LONG_CONNECTION, 0, "");
    }


}
