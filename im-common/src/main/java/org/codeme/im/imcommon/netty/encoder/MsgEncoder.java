package org.codeme.im.imcommon.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.codeme.im.imcommon.model.vo.ProtocolMsg;

/**
 *  HeartbeatEncoder
 *
 *  心跳ping消息编码器,发送ping命令消息到s端,s端接收到消息后返回pong命令消息
 * @author walker lee
 * @date 2020/5/17
 */
public class MsgEncoder extends MessageToByteEncoder<ProtocolMsg> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtocolMsg protocolMsg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(protocolMsg.getCmdType());
        byteBuf.writeLong(protocolMsg.getSenderId());
        byteBuf.writeLong(protocolMsg.getReceiverId());
        byteBuf.writeLong(protocolMsg.getChatroomId());
        byteBuf.writeInt(protocolMsg.getProtocolVersion());
        byteBuf.writeInt(protocolMsg.getContentLength());
        byteBuf.writeBytes(protocolMsg.getMsgContent().getBytes());
    }
}
