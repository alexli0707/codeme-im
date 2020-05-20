package org.codeme.im.imcommon.http.exp;

/**
 * HzHttpErrorResponseInterface
 *
 * @author walker lee
 * @date 2019-10-29
 */
public interface RestHttpErrorResponseInterface {
    String getMessage();

    int getCode();

    int getHttpStatusCode();
}
