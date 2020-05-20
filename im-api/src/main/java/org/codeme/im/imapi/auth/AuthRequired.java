package org.codeme.im.imapi.auth;


import org.codeme.im.imcommon.http.auth.AuthType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AuthRequired
 *
 * @author walker lee
 * @date 2019-11-07
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRequired {
    AuthType authType() default AuthType.LOGIN;
}
