package org.codeme.im.imcommon.constant;

import org.codeme.im.imcommon.http.exp.RestHttpErrorResponseInterface;
import org.apache.http.HttpStatus;

/**
 * HzHttpErrorResponse
 *
 * @author walker lee
 * @date 2019-07-16
 */
public enum RestHttpErrorResponseEnum implements RestHttpErrorResponseInterface {
    WARNING_UNAUTHORIZED("未授权用户", 10001, HttpStatus.SC_UNAUTHORIZED),
    INTERNAL_API_ERROR("内部请求调用异常", 10002, HttpStatus.SC_INTERNAL_SERVER_ERROR),
    INVALID_PARAMS_REQUEST("非法参数", 10003, HttpStatus.SC_BAD_REQUEST),
    INTERNAL_SERVER_ERROR("服务器内部错误", 10004, HttpStatus.SC_INTERNAL_SERVER_ERROR),
    NOT_FOUND("资源不存在", 10005, HttpStatus.SC_NOT_FOUND),
    URI_NOT_FOUND("uri不存在", 404, HttpStatus.SC_NOT_FOUND),
    FORBBIDEN("您不具有对应权限", 10006, HttpStatus.SC_FORBIDDEN),
    PASSWORD_NOT_EQUAL_CONFIRM("密码不一致", 1006, HttpStatus.SC_BAD_REQUEST),
    MAX_UPLOAD_SIZE_EXCEEDED("上传文件太大", 10007, HttpStatus.SC_BAD_REQUEST),
    UNKNOWN_AUTH_TYPE("未知AuthType", 10008, HttpStatus.SC_BAD_REQUEST),
    AUTH_TOKEN_GENERATE_FAILURE("生成token失败", 10009, HttpStatus.SC_INTERNAL_SERVER_ERROR),
    INVALID_TOKEN("无效token", 100010, HttpStatus.SC_BAD_REQUEST),
    INVALID_USER_STATUS("无效用户状态", 100011, HttpStatus.SC_INTERNAL_SERVER_ERROR),
    OAUTH_FAILURE("用户名或者密码错误", 100012, HttpStatus.SC_BAD_REQUEST),
    EXISTED_USERNAME("用户名已存在", 100013, HttpStatus.SC_BAD_REQUEST),
    USERNAME_TOO_LONG("帐号过长,请输入最多不超过%d位的帐号", 1007, HttpStatus.SC_BAD_REQUEST),
    PASSWORD_TOO_LONG("密码过长,请输入最多不超过%d位的帐号", 1008, HttpStatus.SC_BAD_REQUEST),
    REFRESH_TOKEN_INVALID("账号已在其他设备登录，请重新登录", 100015, HttpStatus.SC_BAD_REQUEST),
    ;

    private String message;
    private int code;
    private int httpStatusCode;


    RestHttpErrorResponseEnum(String message, int code, int httpStatusCode) {
        this.message = message;
        this.code = code;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
