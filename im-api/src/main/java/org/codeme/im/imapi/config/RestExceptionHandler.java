package org.codeme.im.imapi.config;

import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imcommon.constant.RestHttpErrorResponseEnum;
import org.codeme.im.imcommon.http.exp.RestHttpException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * HZRestExecptionHandler
 *
 * @author walker lee
 * @date 2019/7/4
 */

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RestHttpException.class)
    protected ResponseEntity<Object> handleHZHttpException(
            RestHttpException ex) {

        return buildResponseEntity(ex);
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleCommonsException(Exception e) {
        if (e instanceof MaxUploadSizeExceededException) {
            return buildResponseEntity(new RestHttpException(RestHttpErrorResponseEnum.MAX_UPLOAD_SIZE_EXCEEDED));
        }

        log.error("Unexpected error in ", e);
        return buildResponseEntity(new RestHttpException(RestHttpErrorResponseEnum.INTERNAL_SERVER_ERROR));
    }


    private ResponseEntity<Object> buildResponseEntity(RestHttpException ex) {
        return ResponseEntity.status(ex.getHttpStatusCode()).header("Content-Type","application/json;charset=UTF-8").body(ex.getResponseBody());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return  buildResponseEntity(new RestHttpException(RestHttpErrorResponseEnum.URI_NOT_FOUND));
    }
}
