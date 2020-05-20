package org.codeme.im.imapi.constant;


import org.codeme.im.imcommon.constant.RestHttpErrorResponseEnum;
import org.codeme.im.imcommon.http.exp.RestHttpException;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * UserStatusEnum
 * 用户状态
 *
 * @author walker lee
 * @date 2019-10-10
 */
public enum UserStatusEnum {
    //VIP是android和ios双料会员
    FORBIDDEN(-1), INACTIVE(0), NORMAL(1), VIP(2);

    private static final Map<Integer, UserStatusEnum> STATUS_MAP =
            Arrays.stream(UserStatusEnum.values())
                    .collect(Collectors.toMap(UserStatusEnum::getStatus, e -> e));

    private int status;

    UserStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static UserStatusEnum getByStatus(int status) throws RestHttpException {
        UserStatusEnum userStatusEnum = STATUS_MAP.get(status);
        if (null == userStatusEnum) {
            throw new RestHttpException(RestHttpErrorResponseEnum.INVALID_USER_STATUS);
        }
        return userStatusEnum;
    }

    public static boolean isValid(int status) throws RestHttpException {
        UserStatusEnum userStatusEnum = getByStatus(status);
        return userStatusEnum.status >= NORMAL.status;
    }
}
