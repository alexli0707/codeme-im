package org.codeme.im.imapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.codeme.im.imapi.entity.ChatroomMember;

import java.util.List;

/**
 * <p>
 * 群聊成员 服务类
 * </p>
 *
 * @author codeme
 * @since 2020-05-26
 */
public interface ChatroomMemberService extends IService<ChatroomMember> {

    public void batchInsertOnDuplicateUpdate(List<ChatroomMember> chatroomMemberList);

}
