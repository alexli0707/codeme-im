package org.codeme.im.imapi.controller;

import org.codeme.im.imapi.auth.AuthRequired;
import org.codeme.im.imapi.entity.Chatroom;
import org.codeme.im.imapi.entity.ChatroomMember;
import org.codeme.im.imapi.service.ChatroomMemberService;
import org.codeme.im.imapi.service.ChatroomService;
import org.codeme.im.imapi.service.impl.ChatRoomTransactionService;
import org.codeme.im.imapi.util.AuthUtil;
import org.codeme.im.imapi.util.ParamsObject;
import org.codeme.im.imcommon.constant.RestHttpErrorResponseEnum;
import org.codeme.im.imcommon.constant.UriConstant;
import org.codeme.im.imcommon.http.RestHttpResponse;
import org.codeme.im.imcommon.http.auth.AuthType;
import org.codeme.im.imcommon.http.exp.RestHttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ChatroomController
 *
 * @author walker lee
 * @date 2020/5/26
 */
@RestController
@RequestMapping(UriConstant.CHATROOM)
public class ChatroomController {

    @Autowired
    ChatroomService chatroomService;

    @Autowired
    ChatroomMemberService chatroomMemberService;

    @Autowired
    ChatRoomTransactionService chatRoomTransactionService;

    /**
     * 创建群聊,必要传参`title`
     *
     * @param paramsObject
     * @param httpServletRequest
     * @return
     * @throws RestHttpException
     */
    @PostMapping("")
    @AuthRequired(authType = AuthType.LOGIN)
    public RestHttpResponse createChatroom(@RequestBody ParamsObject paramsObject, HttpServletRequest httpServletRequest) throws RestHttpException {
        Long userId = AuthUtil.getUserId(httpServletRequest);
        String title = paramsObject.getString("title");
        if (StringUtils.isEmpty(title)) {
            throw new RestHttpException(RestHttpErrorResponseEnum.INVALID_PARAMS_REQUEST);
        }
        Chatroom chatroom = new Chatroom(title, userId);
        chatRoomTransactionService.createChatroom(chatroom, userId);
        return RestHttpResponse.Builder().dataResponse(chatroom).build();
    }

    @PostMapping("/invite")
    @AuthRequired(authType = AuthType.LOGIN)
    public RestHttpResponse inviteUsers(@RequestBody ParamsObject paramsObject, HttpServletRequest httpServletRequest) throws RestHttpException {
        Long userId = AuthUtil.getUserId(httpServletRequest);
        List<Long> inviteeList = paramsObject.getList("invitee", Long.class);
        Long chatroomId = paramsObject.getLong("chatroom_id");
        if (CollectionUtils.isEmpty(inviteeList) || null == chatroomId || 0 == chatroomId) {
            throw new RestHttpException(RestHttpErrorResponseEnum.INVALID_PARAMS_REQUEST);
        }
        Chatroom chatroom = chatroomService.getById(chatroomId);
        if (null == chatroom || !chatroom.getOwnerUserId().equals(userId)) {
            throw new RestHttpException(RestHttpErrorResponseEnum.INVALID_PARAMS_REQUEST);
        }
        List<ChatroomMember> chatroomMemberList = inviteeList.stream().map(aLong -> new ChatroomMember(chatroomId, aLong)).collect(Collectors.toList());
        chatroomMemberService.batchInsertOnDuplicateUpdate(chatroomMemberList);
        return RestHttpResponse.Builder().dataResponse(null).build();
    }


}
