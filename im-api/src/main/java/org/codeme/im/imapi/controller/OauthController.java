package org.codeme.im.imapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.codeme.im.imapi.config.ApiProjectProperties;
import org.codeme.im.imapi.constant.*;
import org.codeme.im.imapi.controller.validator.UserValidator;
import org.codeme.im.imapi.entity.AccessToken;
import org.codeme.im.imapi.entity.User;
import org.codeme.im.imapi.service.AccessTokenService;
import org.codeme.im.imapi.service.UserService;
import org.codeme.im.imapi.util.*;
import org.codeme.im.imcommon.constant.RedisKeyConstant;
import org.codeme.im.imcommon.constant.RestHttpErrorResponseEnum;
import org.codeme.im.imcommon.constant.UriConstant;
import org.codeme.im.imcommon.http.RestHttpResponse;
import org.codeme.im.imcommon.http.exp.RestHttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 授权token 前端控制器
 * </p>
 *
 * @author codeme
 * @since 2020-05-19
 */
@RestController
@RequestMapping(UriConstant.OAUTH)
@Slf4j
public class OauthController {
    @Autowired
    UserValidator userValidator;

    @Autowired
    UserService userService;

    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    ApiProjectProperties projectProperties;

    //生成token 重试次数
    private static final Integer MAX_GENERATE_TOKEN_LIMIT = 5;

    /**
     * 注册接口
     *
     * @param paramsObject
     * @param servletRequest
     * @return
     * @throws RestHttpException
     */
    @PostMapping("/register")
    public RestHttpResponse register(@RequestBody ParamsObject paramsObject, HttpServletRequest servletRequest) throws RestHttpException {
        String username = paramsObject.getString("username");
        String password = paramsObject.getString("password");
        //password是aes加密的,先转码
        String confirmedPassword = paramsObject.getString("confirmed_password");
        //判断帐号是否已经被注册过
        User tmp = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if (null != tmp) {
            throw new RestHttpException(RestHttpErrorResponseEnum.EXISTED_USERNAME);
        }
        userValidator.validUserRegistrationAndThrowError(username, password, confirmedPassword);
        String originPassword = null;
        //是否是aes加密密码的请求
        if (projectProperties.isApiPasswordAesTrans()) {
            originPassword = AESUtil.decrypt(password);
        } else {
            originPassword = password;
        }
        String encryptedPassword = PasswordUtil.encryptPasswordWithBycrypt(originPassword);
        User user = new User(username, encryptedPassword, UserStatusEnum.NORMAL.getStatus());
        if (RegexUtil.isMobileNO(username)) {
            user.setMobile(username);
        }
        userService.save(user);
        return RestHttpResponse.Builder().dataResponse(null).build();
    }

    /**
     * 登录授权
     *
     * @param paramsObject
     * @param servletRequest
     * @return
     * @throws RestHttpException
     */
    @PostMapping("")
    public RestHttpResponse oauth(@RequestBody ParamsObject paramsObject, HttpServletRequest servletRequest) throws RestHttpException {
        String username = paramsObject.getString("username");
        String password = paramsObject.getString("password");
        userValidator.validUserRegistrationAndThrowError(username, password);
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        String originPassword = null;
        //是否是aes加密密码的请求
        if (projectProperties.isApiPasswordAesTrans()) {
            originPassword = AESUtil.decrypt(password);
        } else {
            originPassword = password;
        }
        if (null == user || !PasswordUtil.matchPasswordWithBycrypt(originPassword, user.getPassword())) {
            throw new RestHttpException(RestHttpErrorResponseEnum.OAUTH_FAILURE);
        }
        Long userId = user.getId();
        AccessToken preToken = accessTokenService.getOne(new QueryWrapper<AccessToken>().eq("user_id", userId));
        if (null != preToken) {
            redisTemplate.delete(RedisKeyConstant.getAccessTokenKey(preToken.getAccessToken()));
        }
        Map<String, Object> rs = generateAccessToken(userId, servletRequest);

        return RestHttpResponse.Builder().dataResponse(rs).build();
    }

    private Map<String, Object> generateAccessToken(Long userId, HttpServletRequest servletRequest) throws RestHttpException {
        String accessToken = UuidUtils.getUUID();
        String refreshToken = UuidUtils.getUUID();
        int count = 0;
        while (count < MAX_GENERATE_TOKEN_LIMIT) {
            boolean success = true;
            AccessToken exist = accessTokenService.getOne(new QueryWrapper<AccessToken>().eq("access_token", accessToken));
            if (null != exist) {
                log.error("access_token生成碰撞");
                accessToken = UuidUtils.getUUID();
                success = false;
            }
            AccessToken refreshTokenExist = accessTokenService.getOne(new QueryWrapper<AccessToken>().eq("refresh_token", refreshToken));
            if (null != refreshTokenExist) {
                log.error("refresh_token生成碰撞");
                refreshToken = UuidUtils.getUUID();
                success = false;
            }
            if (success) {
                break;
            }
            count++;
        }
        if (count == MAX_GENERATE_TOKEN_LIMIT) {
            throw new RestHttpException(RestHttpErrorResponseEnum.AUTH_TOKEN_GENERATE_FAILURE);
        }
        int expiresIn = ApiProjectConstant.TOKEN_EXPIRE_SECOND_PRODUCTION;
        AccessToken token = new AccessToken(userId, accessToken, refreshToken, expiresIn);
        accessTokenService.saveOrUpdate(token, new UpdateWrapper<AccessToken>().eq("user_id", userId));
        Map<String, Object> rs = new HashMap<>();
        rs.put("access_token", token.getAccessToken());
        rs.put("refresh_token", token.getRefreshToken());
        rs.put("expires_in", token.getExpiresIn());
        rs.put("token_type", ApiProjectConstant.TOKEN_TYPE);
        redisTemplate.opsForValue().set(RedisKeyConstant.getAccessTokenKey(accessToken), String.valueOf(userId), token.getExpiresIn(), TimeUnit.SECONDS);
        return rs;
    }

    @PostMapping("/refresh")
    public RestHttpResponse refreshToken(@RequestBody ParamsObject paramsObject, HttpServletRequest servletRequest) throws RestHttpException {
        String refreshToken = paramsObject.getString("refresh_token");
        if (StringUtils.isEmpty(refreshToken)) {
            throw new RestHttpException(RestHttpErrorResponseEnum.INVALID_PARAMS_REQUEST);
        }
        AccessToken preToken = accessTokenService.getOne(new QueryWrapper<AccessToken>().eq("refresh_token", refreshToken));
        if (null == preToken) {
            throw new RestHttpException(RestHttpErrorResponseEnum.REFRESH_TOKEN_INVALID);
        }
        //互踢新逻辑
        Long userId = preToken.getUserId();
        redisTemplate.delete(RedisKeyConstant.getAccessTokenKey(preToken.getAccessToken()));
        redisTemplate.delete(RedisKeyConstant.getRefreshTokenKey(preToken.getRefreshToken()));
        Map<String, Object> rs = generateAccessToken(userId, servletRequest);
        return RestHttpResponse.Builder().dataResponse(rs).build();
    }

}

