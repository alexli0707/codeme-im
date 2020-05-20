package org.codeme.im.imcommon.http;

import java.io.Serializable;

/**
 * HZMeta
 *
 * @author walker lee
 * @date 2019/7/4
 */
public class RestMeta implements Serializable {

    private int code;
    private String message;

    public RestMeta() {
    }

    public RestMeta(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
