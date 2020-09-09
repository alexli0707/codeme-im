package org.codeme.im.imcommon.util;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.codeme.im.imcommon.model.vo.GoogleProtocolMsg;

/**
 * GoogleProtocolUtil
 *
 * @author walker lee
 * @date 2020/8/13
 */
public class GoogleProtocolUtil {

    public static void main(String[] args) throws InvalidProtocolBufferException {

        String msgContent = "testmsg";

        GoogleProtocolMsg.ProtocolMsg protocolMsg = GoogleProtocolMsg.ProtocolMsg.newBuilder().setCmdType(1)
                .setSenderId(1).setReceiverId(2).setChatroomId(0).setProtocolVersion(2)
                .setContentLength(msgContent.getBytes().length).setMsgContent(msgContent).build();

        byte[] encode = encode(protocolMsg);
        System.out.println(encode.length);
        GoogleProtocolMsg.ProtocolMsg parsedMsg = decode(encode);
        System.out.println(protocolMsg.toString());
        System.out.println(protocolMsg.toString().equals(parsedMsg.toString()));

        ByteBuf byteBuf = Unpooled.buffer(512);
        byteBuf.writeInt(protocolMsg.getCmdType());
        byteBuf.writeLong(protocolMsg.getSenderId());
        byteBuf.writeLong(protocolMsg.getReceiverId());
        byteBuf.writeLong(protocolMsg.getChatroomId());
        byteBuf.writeInt(protocolMsg.getProtocolVersion());
        byteBuf.writeInt(protocolMsg.getContentLength());
        byteBuf.writeBytes(protocolMsg.getMsgContent().getBytes());
        System.out.println(byteBuf.writerIndex());
        byteBuf.clear();


    }



    /**
     * 编码
     * @param protocol
     * @return
     */
    public static byte[] encode(GoogleProtocolMsg.ProtocolMsg protocol){
        return protocol.toByteArray() ;
    }
    /**
     * 解码
     * @param bytes
     * @return
     * @throws InvalidProtocolBufferException
     */
    public static GoogleProtocolMsg.ProtocolMsg decode(byte[] bytes) throws InvalidProtocolBufferException {
        return GoogleProtocolMsg.ProtocolMsg.parseFrom(bytes);
    }
    
}
