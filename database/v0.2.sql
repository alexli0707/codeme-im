# 群信息表
CREATE TABLE `chatroom`
(
    `id`                   BIGINT unsigned  NOT NULL AUTO_INCREMENT,
    `title`                varchar(128)     NOT NULL COMMENT '群名称',
    `notice`               TEXT COMMENT '群通知',
    `owner_user_id`        BIGINT UNSIGNED  NOT NULL COMMENT '群主id',
    `owner_group_nickname` VARCHAR(128) COMMENT '群主群昵称',
    `current_count`        INTEGER UNSIGNED NOT NULL DEFAULT 1 COMMENT '群人数',
    `status`               tinyint(4)                DEFAULT '1' COMMENT '状态:0 封禁 ,1 正常',
    `create_at`            timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_at`            timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `ik_owner_user_id` (`owner_user_id`),
    INDEX `ik_title_owner_user_id` (`owner_user_id`, `title`)
) ENGINE = InnoDB COMMENT ='群信息表';

# 群成员
CREATE TABLE `chatroom_member`
(
    `id`          BIGINT unsigned NOT NULL AUTO_INCREMENT,
    `chatroom_id` BIGINT unsigned NOT NULL COMMENT '群id',
    `user_id`     BIGINT UNSIGNED NOT NULL COMMENT '用户id',
    `group_nickname` VARCHAR(128) COMMENT '群成员群昵称',
    `status`               tinyint(4)                DEFAULT '1' COMMENT '状态:0 禁言 ,1 正常',
    `create_at`            timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_at`            timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE `uk_chatroom_id_user_id`(`chatroom_id`,`user_id`)

) ENGINE = InnoDB COMMENT ='群聊成员';
