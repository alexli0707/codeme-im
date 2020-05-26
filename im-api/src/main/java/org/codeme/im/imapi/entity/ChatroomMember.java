package org.codeme.im.imapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 群聊成员
 * </p>
 *
 * @author codeme
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ChatroomMember implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 群id
     */
    private Long chatroomId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 群成员群昵称
     */
    private String groupNickname;

    /**
     * 状态:0 禁言 ,1 正常
     */
    private Integer status;

    private Date createAt;

    private Date updateAt;

    public ChatroomMember() {
    }

    public ChatroomMember(Long chatroomId, Long userId) {
        this.chatroomId = chatroomId;
        this.userId = userId;
    }
}
