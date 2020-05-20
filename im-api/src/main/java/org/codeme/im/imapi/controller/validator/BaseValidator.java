package org.codeme.im.imapi.controller.validator;

import org.codeme.im.imcommon.constant.RestHttpErrorResponseEnum;
import org.codeme.im.imcommon.http.exp.RestHttpException;
import org.springframework.util.StringUtils;

/**
 * BaseValidator
 *
 * @author walker lee
 * @date 2019-10-10
 */
public class BaseValidator {
    public boolean validStringAndThrowError(String content) throws RestHttpException {
        if (StringUtils.isEmpty(content)) {
            throw new RestHttpException(RestHttpErrorResponseEnum.INVALID_PARAMS_REQUEST);
        }
        return true;
    }
}
