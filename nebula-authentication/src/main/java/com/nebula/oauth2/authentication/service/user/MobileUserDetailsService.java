package com.nebula.oauth2.authentication.service.user;

import com.nebula.oauth2.authentication.entity.User;
import com.nebula.oauth2.authentication.service.UserTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;

/**
 *
 * @author feifeixia
 * 2019/5/14 13:56
 */
@Service
public class MobileUserDetailsService extends AbstractUserDetailService {

    @Autowired
    private UserTransferService userTransferService;

    @Override
    protected User getUserVO(final String username) {
        final User user = userTransferService.findUserByMobile(username);
        if (user == null) {
            throw new InternalAuthenticationServiceException("手机号: " + username + ", 不存在");
        }
        return user;
    }
}
