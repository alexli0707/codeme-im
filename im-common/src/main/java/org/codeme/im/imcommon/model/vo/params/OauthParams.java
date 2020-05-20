package org.codeme.im.imcommon.model.vo.params;

import lombok.Data;

/**
 * OauthParams
 *
 * @author walker lee
 * @date 2020/5/20
 */
@Data
public class OauthParams {
    private String username;
    private String password;

    public OauthParams(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
