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
 * 群信息表
 * </p>
 *
 * @author codeme
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Chatroom implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 群名称
     */
    private String title;

    /**
     * 群通知
     */
    private String notice;

    /**
     * 群主id
     */
    private Long ownerUserId;

    /**
     * 群主群昵称
     */
    private String ownerGroupNickname;

    /**
     * 群人数
     */
    private Integer currentCount;

    /**
     * 状态:0 封禁 ,1 正常
     */
    private Integer status;

    private Date createAt;

    private Date updateAt;


    public Chatroom() {
    }

    public Chatroom(String title, Long ownerUserId) {
        this.title = title;
        this.ownerUserId = ownerUserId;
    }
}
