package org.codeme.im.imapi.service.impl;

import org.codeme.im.imapi.entity.Chatroom;
import org.codeme.im.imapi.entity.ChatroomMember;
import org.codeme.im.imapi.service.ChatroomMemberService;
import org.codeme.im.imapi.service.ChatroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ChatRoomTransactionService
 *
 * @author walker lee
 * @date 2020/5/26
 */
@Service
public class ChatRoomTransactionService {

    @Autowired
    ChatroomService chatroomService;

    @Autowired
    ChatroomMemberService chatroomMemberService;

    @Transactional
    public void createChatroom(Chatroom chatroom, Long userId) {
        chatroomService.save(chatroom);
        List<ChatroomMember> chatroomMemberList = new ArrayList<>();
        chatroomMemberList.add(new ChatroomMember(chatroom.getId(), userId));
        chatroomMemberService.batchInsertOnDuplicateUpdate(chatroomMemberList);
    }

}
