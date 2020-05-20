package org.codeme.im.imapi.service.impl;

import org.codeme.im.imapi.entity.AccessToken;
import org.codeme.im.imapi.mapper.AccessTokenMapper;
import org.codeme.im.imapi.service.AccessTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 授权token 服务实现类
 * </p>
 *
 * @author codeme
 * @since 2020-05-19
 */
@Service
public class AccessTokenServiceImpl extends ServiceImpl<AccessTokenMapper, AccessToken> implements AccessTokenService {

}
