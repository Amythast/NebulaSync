package com.nebula.oauth2.authentication.service.user;

import com.nebula.oauth2.authentication.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 抽象UserDetailsService 满足不同的登录方式
 *
 * @author feifeixia
 * 2019/5/14 11:36
 */
@Slf4j
public abstract class AbstractUserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserVO(username);
    }

    /**
     * 获取 User 对象
     *
     * @param username 用户名
     * @return
     */
    protected abstract User getUserVO(String username);
}
