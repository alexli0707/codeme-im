package org.codeme.im.imapi.service.impl;

import org.codeme.im.imapi.entity.ChatroomMember;
import org.codeme.im.imapi.mapper.ChatroomMemberMapper;
import org.codeme.im.imapi.service.ChatroomMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 群聊成员 服务实现类
 * </p>
 *
 * @author codeme
 * @since 2020-05-26
 */
@Service
public class ChatroomMemberServiceImpl extends ServiceImpl<ChatroomMemberMapper, ChatroomMember> implements ChatroomMemberService {

    @Override
    public void batchInsertOnDuplicateUpdate(List<ChatroomMember> chatroomMemberList) {
        this.getBaseMapper().batchInsertOnDuplicateUpdate(chatroomMemberList);
    }
}
