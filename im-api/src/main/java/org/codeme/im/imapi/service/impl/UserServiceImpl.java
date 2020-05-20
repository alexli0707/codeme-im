package org.codeme.im.imapi.service.impl;

import org.codeme.im.imapi.entity.User;
import org.codeme.im.imapi.mapper.UserMapper;
import org.codeme.im.imapi.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author codeme
 * @since 2020-05-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
