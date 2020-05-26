package org.codeme.im.imapi.service;

import org.codeme.im.imapi.entity.Chatroom;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 群信息表 服务类
 * </p>
 *
 * @author codeme
 * @since 2020-05-26
 */
public interface ChatroomService extends IService<Chatroom> {

    Integer increaseRoomMemberCount(Long id, Integer increaseCount);

}
