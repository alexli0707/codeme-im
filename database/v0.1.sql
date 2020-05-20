# 用户
CREATE TABLE `user` (
                        `id` BIGINT unsigned NOT NULL AUTO_INCREMENT,
                        `username` varchar(128) NOT NULL COMMENT '用户名',
                        `password` varchar(255) DEFAULT NULL COMMENT '加密加盐后的密码',
                        `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
                        `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
                        `status` tinyint(4) DEFAULT '0' COMMENT '状态: -1 禁用,0 未激活,1 正常',
                        `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `uk_username_auth_type` (`username`),
                        UNIQUE KEY `uk_mobile` (`mobile`)
) ENGINE=InnoDB  COMMENT='用户表';


# token
CREATE TABLE `access_token` (
                                `id` BIGINT unsigned NOT NULL AUTO_INCREMENT,
                                `user_id` BIGINT unsigned NOT NULL COMMENT '用户id',
                                `access_token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'token',
                                `refresh_token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '刷新token',
                                `expires_in` int(10) unsigned NOT NULL DEFAULT '3600' COMMENT '过期时间,默认是秒',
                                `token_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'bearer' COMMENT 'token类型',
                                `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                `channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '渠道',
                                `os_type` tinyint(3) unsigned DEFAULT '0' COMMENT '系统类型: 1-android(手机) 2-ios 3-web  4-android(平板) 5-ipad',
                                `manufacturer` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '厂商名称',
                                `device` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备名称',
                                `ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录ip',
                                `country` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家',
                                `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
                                `area` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地区',
                                `region` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区域',
                                `isp` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '运营商',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `uk_user_id_os_type` (`user_id`,`os_type`),
                                KEY `ik_access_token` (`access_token`),
                                KEY `ik_refresh_token` (`refresh_token`)
) ENGINE=InnoDB  COMMENT='授权token';
