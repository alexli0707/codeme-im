package org.codeme.im.imcommon.constant;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * ServerStatusCode
 *
 * @author walker lee
 * @date 2020/5/20
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ServerStatusCode {

    ERROR_TOKEN(10001, "token异常");

    private int code;
    private String msg;

    ServerStatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
