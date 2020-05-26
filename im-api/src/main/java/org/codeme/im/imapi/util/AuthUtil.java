package org.codeme.im.imapi.util;

import org.codeme.im.imapi.constant.ApiProjectConstant;

import javax.servlet.http.HttpServletRequest;

/**
 * AuthUtil
 *
 * @author walker lee
 * @date 2020/5/26
 */
public class AuthUtil {
    public static Long getUserId(HttpServletRequest httpServletRequest) {
        Long id = Long.parseLong((String) httpServletRequest.getAttribute(ApiProjectConstant.KEY_USER_ID_ATTR));
        return id;
    }
}
