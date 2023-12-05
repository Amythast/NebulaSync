package com.nebula.oauth2.authentication.service.user;

import com.nebula.oauth2.authentication.entity.User;
import com.nebula.oauth2.authentication.service.UserTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户信息获取
 *
 * @author feifeixia
 * @date 2018/7/24 17:06
 */
@Service
public class UserNameUserDetailsServiceImpl extends AbstractUserDetailService {

    @Autowired
    private UserTransferService userTransferService;

    @Override
    protected User getUserVO(final String username) {
        // 查询用户信息,包含角色列表
        User user = userTransferService.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名/密码错误");
        }
        return user;
    }

}
