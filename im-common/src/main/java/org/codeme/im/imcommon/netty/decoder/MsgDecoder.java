package org.codeme.im.imcommon.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.codeme.im.imcommon.model.vo.ProtocolMsg;

import java.util.List;

/**
 * MsgConstant
 * 消息结构:消息头 + 消息体
 * 消息头结构:  cmd_type(4 bytes:int)| sender_id(8 bytes:long)  |receiver_id(8 bytes:long) | protocol_version:消息版本(4 bytes:int)|content_length(4 bytes)
 *
 * @author walker lee
 * @date 2020/5/15
 */
public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int cmdType = byteBuf.readInt();
        long sendId = byteBuf.readLong();
        long receiverId = byteBuf.readLong();
        long chatroomId = byteBuf.readLong();
        int protocolVersion = byteBuf.readInt();
        int contentLength = byteBuf.readInt();
        byte[] msgContentBytes = new byte[contentLength];
        byteBuf.readBytes(msgContentBytes);
        ProtocolMsg protocolMsg = new ProtocolMsg(cmdType, sendId, receiverId, chatroomId, protocolVersion, contentLength, new String(msgContentBytes));
        list.add(protocolMsg);
    }
}
