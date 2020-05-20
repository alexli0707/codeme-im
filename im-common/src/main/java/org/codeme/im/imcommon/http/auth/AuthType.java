package org.codeme.im.imcommon.http.auth;

/**
 * AuthType
 *
 * 鉴权类型: PUBLIC-公共接口    BASIC-使用basic auth,由于现在都在konga上利用插件鉴权,BASIC也无需鉴权,  LOGIN-需要对于HEADER accesstoken鉴权
 * @author walker lee
 * @date 2019-09-04
 */
public enum AuthType {
    LOGIN, BASIC, PUBLIC
}
