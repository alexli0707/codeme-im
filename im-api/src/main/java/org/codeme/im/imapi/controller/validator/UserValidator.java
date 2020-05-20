package org.codeme.im.imapi.controller.validator;

import org.codeme.im.imcommon.constant.RestHttpErrorResponseEnum;
import org.codeme.im.imcommon.http.exp.RestHttpException;
import org.springframework.stereotype.Component;

/**
 * UserValidator
 *
 * @author walker lee
 * @date 2019-10-10
 */
@Component
public class UserValidator extends BaseValidator {
    private static final int USERNAME_MAX_LENGTH = 128;
    private static final int PASSWORD_MAX_LENGTH = 255;


    public boolean validUserRegistrationAndThrowError(String username, String password, String confirmedPassword) throws RestHttpException {
        validStringAndThrowError(username);
        validStringAndThrowError(password);
        validStringAndThrowError(confirmedPassword);
        //判断密码
        if (!password.equals(confirmedPassword)) {
            throw new RestHttpException(RestHttpErrorResponseEnum.PASSWORD_NOT_EQUAL_CONFIRM);
        }
        //判断帐号
        if (username.length() > USERNAME_MAX_LENGTH) {
            throw new RestHttpException(String.format(RestHttpErrorResponseEnum.USERNAME_TOO_LONG.getMessage(), USERNAME_MAX_LENGTH),
                    RestHttpErrorResponseEnum.USERNAME_TOO_LONG.getCode(), RestHttpErrorResponseEnum.USERNAME_TOO_LONG.getHttpStatusCode());
        }
        //判断密码
        if (password.length() > PASSWORD_MAX_LENGTH) {
            throw new RestHttpException(String.format(RestHttpErrorResponseEnum.PASSWORD_TOO_LONG.getMessage(), PASSWORD_MAX_LENGTH),
                    RestHttpErrorResponseEnum.PASSWORD_TOO_LONG.getCode(), RestHttpErrorResponseEnum.PASSWORD_TOO_LONG.getHttpStatusCode());
        }
        return true;
    }


    public boolean validUserRegistrationAndThrowError(String username, String password) throws RestHttpException {
        validStringAndThrowError(username);
        validStringAndThrowError(password);
        //判断帐号
        if (username.length() > USERNAME_MAX_LENGTH) {
            throw new RestHttpException(String.format(RestHttpErrorResponseEnum.USERNAME_TOO_LONG.getMessage(), USERNAME_MAX_LENGTH),
                    RestHttpErrorResponseEnum.USERNAME_TOO_LONG.getCode(), RestHttpErrorResponseEnum.USERNAME_TOO_LONG.getHttpStatusCode());
        }
        //判断密码
        if (password.length() > PASSWORD_MAX_LENGTH) {
            throw new RestHttpException(String.format(RestHttpErrorResponseEnum.PASSWORD_TOO_LONG.getMessage(), PASSWORD_MAX_LENGTH),
                    RestHttpErrorResponseEnum.PASSWORD_TOO_LONG.getCode(), RestHttpErrorResponseEnum.PASSWORD_TOO_LONG.getHttpStatusCode());
        }
        return true;
    }
}
