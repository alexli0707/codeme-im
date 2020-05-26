package org.codeme.im.imapi.service.impl;

import org.codeme.im.imapi.entity.Chatroom;
import org.codeme.im.imapi.entity.ChatroomMember;
import org.codeme.im.imapi.service.ChatroomMemberService;
import org.codeme.im.imapi.service.ChatroomService;
import org.codeme.im.imcommon.constant.RedisKeyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    RedisTemplate redisTemplate;


    @Transactional
    public void createChatroom(Chatroom chatroom, Long userId) {
        chatroomService.save(chatroom);
        List<ChatroomMember> chatroomMemberList = new ArrayList<>();
        chatroomMemberList.add(new ChatroomMember(chatroom.getId(), userId));
        chatroomMemberService.batchInsertOnDuplicateUpdate(chatroomMemberList);
        redisTemplate.opsForSet().add(RedisKeyConstant.getChatroomMembers(chatroom.getId()), userId);
    }


    @Transactional
    public void inviteChatroomMemebers(List<ChatroomMember> chatroomMemberList, Long chatroomId, List<Long> inviteeList) {
        chatroomMemberService.batchInsertOnDuplicateUpdate(chatroomMemberList);
        chatroomService.increaseRoomMemberCount(chatroomId, chatroomMemberList.size());
        redisTemplate.opsForSet().add(RedisKeyConstant.getChatroomMembers(chatroomId), inviteeList.toArray(new Long[0]));

    }

}
