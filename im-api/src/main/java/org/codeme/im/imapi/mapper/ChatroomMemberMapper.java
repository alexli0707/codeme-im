package org.codeme.im.imapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.codeme.im.imapi.entity.ChatroomMember;

import java.util.List;

/**
 * <p>
 * 群聊成员 Mapper 接口
 * </p>
 *
 * @author codeme
 * @since 2020-05-26
 */
public interface ChatroomMemberMapper extends BaseMapper<ChatroomMember> {

    public void batchInsertOnDuplicateUpdate(List<ChatroomMember> chatroomMemberList);

}
