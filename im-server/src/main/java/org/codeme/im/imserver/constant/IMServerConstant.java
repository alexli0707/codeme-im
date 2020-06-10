package org.codeme.im.imserver.constant;

import io.netty.util.AttributeKey;

/**
 * IMServerConstant
 *
 * @author walker lee
 * @date 2020/6/2
 */
public class IMServerConstant {
    public static final String IM_SERVICE_ZK_DIR_PATH = "/services/socket/im-server";

    public static final String IM_OUTER_SERVICE_ZK_DIR_PATH = "/services/outer/socket/im-server";


    public static final String USER_ID = "user_id";

    public static final AttributeKey<Long> KEY_USER_ID = AttributeKey.valueOf("user_id");
}
