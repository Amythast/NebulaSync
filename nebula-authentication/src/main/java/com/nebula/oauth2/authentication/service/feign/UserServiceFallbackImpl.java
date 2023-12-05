package com.nebula.oauth2.authentication.service.feign;

import com.nebula.oauth2.authentication.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户Service Fallback
 *
 * @author feifeixia
 * 2019/7/26 10:14
 */
@Service
@Slf4j
public class UserServiceFallbackImpl implements UserService {

    @Override
    public UserVO findUserByUsername(String username) {
        log.error("通过用户名查询用户异常:{}", username);
        return new UserVO();
    }

    @Override
    public UserVO findUserByMobile(String mobile) {
        log.error("通过手机号查询用户:{}", mobile);
        return new UserVO();
    }
}
