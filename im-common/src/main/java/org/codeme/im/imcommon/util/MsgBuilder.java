package org.codeme.im.imcommon.util;

import org.codeme.im.imcommon.constant.MsgConstant;
import org.codeme.im.imcommon.constant.ServerStatusCode;
import org.codeme.im.imcommon.http.util.JsonTools;
import org.codeme.im.imcommon.model.vo.ProtocolMsg;
import org.codeme.im.imcommon.model.vo.TextMsg;

/**
 * MsgBuilder
 *
 * @author walker lee
 * @date 2020/5/17
 */
public class MsgBuilder {

    public static ProtocolMsg makeMsg(int cmdType, long senderId, long receiverId, long chatroomId, int protocolVersion, int contentLength, String msgContent) {
        ProtocolMsg protocolMsg;
        switch (protocolVersion) {
            case MsgConstant.Version.LONG_CONNECTION:
                protocolMsg = new ProtocolMsg(cmdType, senderId, receiverId, protocolVersion, contentLength, msgContent);
                break;
            case MsgConstant.Version.CHATROOM:
                protocolMsg = new ProtocolMsg(cmdType, senderId, receiverId, chatroomId, protocolVersion, contentLength, msgContent);
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
        return MsgBuilder.makeMsg(MsgConstant.MsgCmdType.PING, senderId, 0, 0, MsgConstant.Version.CHATROOM, 0, "");
    }

    /**
     * 生成pong消息, senderId不关注默认为0
     *
     * @param receiverId
     * @return
     */
    public static ProtocolMsg makePongMsg(long receiverId) {
        return MsgBuilder.makeMsg(MsgConstant.MsgCmdType.PONG, 0, receiverId, 0, MsgConstant.Version.CHATROOM, 0, "");
    }

    /**
     * 生成auth消息
     *
     * @param accessToken
     * @return
     */
    public static ProtocolMsg makeAuthMsg(String accessToken) {
        return MsgBuilder.makeMsg(MsgConstant.MsgCmdType.AUTH, 0, 0, 0, MsgConstant.Version.CHATROOM, accessToken.getBytes().length, accessToken);
    }

    /**
     * 生成auth成功消息,返回当前帐号id
     *
     * @param id
     * @return
     */
    public static ProtocolMsg makeAuthSuccessMsg(long id) {
        String msgContent = String.valueOf(id);
        return MsgBuilder.makeMsg(MsgConstant.MsgCmdType.AUTH_SUCCESS, 0, 0, 0, MsgConstant.Version.CHATROOM, msgContent.getBytes().length, msgContent);
    }

    /**
     * 生成auth失败消息,返回失败原因
     *
     * @return
     */
    public static ProtocolMsg makeAuthFailMsg(ServerStatusCode serverStatusCode) {
        String jsonContent = JsonTools.simpleObjToStr(serverStatusCode);
        return MsgBuilder.makeMsg(MsgConstant.MsgCmdType.AUTH_FAIL, 0, 0, 0, MsgConstant.Version.CHATROOM, jsonContent.getBytes().length, jsonContent);
    }

    /**
     * 发送内部通道请求授权消息
     *
     * @param serverId
     * @return
     */
    public static ProtocolMsg makeRPCAuthMsg(long serverId) {
        return MsgBuilder.makeMsg(MsgConstant.MsgCmdType.RPC_AUTH, serverId, 0, 0, MsgConstant.Version.CHATROOM, 0, "");
    }

    /**
     * 返回内部通道请求授权成功消息
     *
     * @param serverId
     * @return
     */
    public static ProtocolMsg makeRPCAuthSuccessMsg(long serverId) {
        return MsgBuilder.makeMsg(MsgConstant.MsgCmdType.RPC_AUTH_SUCCESS, 0, serverId, 0, MsgConstant.Version.CHATROOM, 0, "");
    }


    /**
     * 生成单点文本消息
     *
     * @param senderId
     * @param receiverId
     * @param textMsg
     * @return
     */
    public static ProtocolMsg makeTextMsg(long senderId, long receiverId, TextMsg textMsg) {
        String jsonContent = JsonTools.simpleObjToStr(textMsg);
        return MsgBuilder.makeMsg(MsgConstant.MsgCmdType.SEND_TEXT_MSG, senderId, receiverId, 0, MsgConstant.Version.CHATROOM, jsonContent.getBytes().length, jsonContent);
    }

    /**
     * 生成单点文本消息服务端ack消息
     *
     * @param senderId
     * @param receiverId
     * @param textMsg
     * @return
     */
    public static ProtocolMsg makeServerAckTextMsg(long senderId, long receiverId, TextMsg textMsg) {
        String jsonContent = JsonTools.simpleObjToStr(textMsg);
        return MsgBuilder.makeMsg(MsgConstant.MsgCmdType.ACK_TEXT_MSG, senderId, receiverId, 0, MsgConstant.Version.CHATROOM, jsonContent.getBytes().length, jsonContent);
    }

    /**
     * 生成群聊文本消息
     *
     * @param senderId
     * @param chatroomId
     * @param textMsg
     * @return
     */
    public static ProtocolMsg makeChatroomTextMsg(long senderId, long receiverId, long chatroomId, TextMsg textMsg) {
        String jsonContent = JsonTools.simpleObjToStr(textMsg);
        return MsgBuilder.makeMsg(MsgConstant.MsgCmdType.SEND_CHATROOM_TEXT_MSG, senderId, receiverId, chatroomId, MsgConstant.Version.CHATROOM, jsonContent.getBytes().length, jsonContent);
    }

    /**
     * 生成群聊ack文本消息
     *
     * @param senderId
     * @param chatroomId
     * @param textMsg
     * @return
     */
    public static ProtocolMsg makeChatroomAckTextMsg(long senderId, long receiverId, long chatroomId, TextMsg textMsg) {
        String jsonContent = JsonTools.simpleObjToStr(textMsg);
        return MsgBuilder.makeMsg(MsgConstant.MsgCmdType.ACK_CHATROOM_TEXT_MSG, senderId, receiverId, chatroomId, MsgConstant.Version.CHATROOM, jsonContent.getBytes().length, jsonContent);
    }

}
