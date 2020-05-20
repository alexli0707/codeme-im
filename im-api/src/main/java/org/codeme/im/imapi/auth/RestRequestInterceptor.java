package org.codeme.im.imapi.auth;

import org.codeme.im.imcommon.constant.RedisKeyConstant;
import org.codeme.im.imcommon.constant.RestHttpErrorResponseEnum;
import org.codeme.im.imapi.entity.User;
import org.codeme.im.imapi.service.UserService;
import org.codeme.im.imcommon.http.auth.AuthType;
import org.codeme.im.imcommon.http.exp.RestHttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * HZRequestFilter
 *
 * @author walker lee
 * @date 2019/7/2
 */
public class RestRequestInterceptor implements HandlerInterceptor {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws RestHttpException {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要登录
        AuthRequired methodAnnotation = method.getAnnotation(AuthRequired.class);
        // 所有非 @AuthRequired 注解, 就是非法接口,除开第三方路由例如actuator.
        if (methodAnnotation != null) {
            AuthType authType = methodAnnotation.authType();
            switch (authType) {
                case PUBLIC:
                    return true;
                case BASIC:
                    return true;
                case LOGIN:
                    String accessToken = request.getHeader("Authorization");
                    if (StringUtils.isEmpty(accessToken)) {
                        throw new RestHttpException(RestHttpErrorResponseEnum.WARNING_UNAUTHORIZED);
                    }
                    accessToken = accessToken.replace("Bearer ", "");

                    String id = (String) redisTemplate.opsForValue().get(RedisKeyConstant.getAccessTokenKey(accessToken));
                    if (null == id) {
                        throw new RestHttpException(RestHttpErrorResponseEnum.INVALID_TOKEN);
                    }
                    User user = userService.getById(id);
                    if (null == user) {
                        throw new RestHttpException(RestHttpErrorResponseEnum.INVALID_TOKEN);
                    }
                    request.setAttribute("currentUser", user);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        throw new RestHttpException(RestHttpErrorResponseEnum.WARNING_UNAUTHORIZED);
//                    }
                    return true;
                default:
                    throw new RestHttpException(RestHttpErrorResponseEnum.UNKNOWN_AUTH_TYPE);
            }
        } else {
            throw new RestHttpException(RestHttpErrorResponseEnum.FORBBIDEN);
        }
    }

}
