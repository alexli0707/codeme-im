package org.codeme.im.imcommon.http.exp;

import org.codeme.im.imcommon.http.RestHttpResponse;
import org.codeme.im.imcommon.http.util.JsonTools;

/**
 * HZHttpExeception
 *
 * @author walker lee
 * @date 2019/7/2
 */
public class RestHttpException extends Exception {
    private String message;
    private int metaCode;
    private int httpStatusCode;
    private RestHttpResponse response;

    public RestHttpException(String message, int metaCode, int httpStatusCode) {
        this.message = message;
        this.metaCode = metaCode;
        this.httpStatusCode = httpStatusCode;
        this.response = RestHttpResponse.Builder().code(metaCode).message(message).build();
    }


    public RestHttpException(RestHttpErrorResponseInterface restHttpErrorResponseInterface) {
        this.message = restHttpErrorResponseInterface.getMessage();
        this.metaCode = restHttpErrorResponseInterface.getCode();
        this.httpStatusCode = restHttpErrorResponseInterface.getHttpStatusCode();
        this.response = RestHttpResponse.Builder().code(metaCode).message(message).build();
    }


    @Override
    public String getMessage() {
        return message;
    }

    public int getMetaCode() {
        return metaCode;
    }


    public String getResponseBody() {
        return JsonTools.simpleObjToStr(this.response);
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
