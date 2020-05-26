package org.codeme.im.imapi.service.impl;

import org.codeme.im.imapi.entity.Chatroom;
import org.codeme.im.imapi.mapper.ChatroomMapper;
import org.codeme.im.imapi.service.ChatroomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群信息表 服务实现类
 * </p>
 *
 * @author codeme
 * @since 2020-05-26
 */
@Service
public class ChatroomServiceImpl extends ServiceImpl<ChatroomMapper, Chatroom> implements ChatroomService {

    @Override
    public Integer increaseRoomMemberCount(Long id, Integer increaseCount) {
        return this.getBaseMapper().increaseRoomMemberCount(id, increaseCount);
    }
}
