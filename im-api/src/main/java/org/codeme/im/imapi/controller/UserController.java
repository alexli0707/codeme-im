package org.codeme.im.imapi.controller;


import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imapi.auth.AuthRequired;
import org.codeme.im.imapi.service.RoundRobbinIMServerService;
import org.codeme.im.imcommon.constant.RestHttpErrorResponseEnum;
import org.codeme.im.imcommon.constant.UriConstant;
import org.codeme.im.imcommon.http.RestHttpResponse;
import org.codeme.im.imcommon.http.auth.AuthType;
import org.codeme.im.imcommon.http.exp.RestHttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author codeme
 * @since 2020-05-19
 */
@RestController
@RequestMapping(UriConstant.USER)
@Slf4j
public class UserController {

    @Autowired
    RoundRobbinIMServerService roundRobbinIMServerService;

    /**
     * 获取可用IMServer地址
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/server")
    @AuthRequired(authType = AuthType.LOGIN)
    public RestHttpResponse getServerUrl(HttpServletRequest httpServletRequest) throws RestHttpException {
        String imServerUrl = roundRobbinIMServerService.getIMServerUrlWithRoundRobbin();
        if (StringUtils.isEmpty(imServerUrl)){
            log.error("没有找到可用imserver");
            throw new RestHttpException(RestHttpErrorResponseEnum.IM_SERVER_INVALID);
        }
        return RestHttpResponse.Builder().dataResponse(imServerUrl).build();
    }

}

