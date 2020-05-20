package org.codeme.im.imcommon.model.vo;

import lombok.Data;

/**
 * AccessToken
 *
 * @author walker lee
 * @date 2020/5/20
 */
@Data
public class AccessToken {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private int expiresIn;
}
