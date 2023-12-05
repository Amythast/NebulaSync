package com.nebula.oauth2.authentication.service;

import cn.hutool.core.collection.CollUtil;
import com.nebula.oauth2.authentication.consts.CommonConstants;
import com.nebula.oauth2.authentication.entity.Operation;
import com.nebula.oauth2.authentication.entity.Role;
import com.nebula.oauth2.authentication.entity.User;
import com.nebula.oauth2.authentication.service.feign.UserService;
import com.nebula.oauth2.authentication.vo.SysRole;
import com.nebula.oauth2.authentication.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 模拟 UserService 实现查询用户
 * 在实际使用上需要替换
 *
 * @author feifeixia
 * 2019/7/3 9:52
 */
@Service
public class UserTransferService {

    @Autowired
    private UserService userService;


    /**
     * 根据用户名称返回用户
     *
     * @param username 用户名称,必须唯一
     * @return
     */
    public User findUserByUsername(String username) {
        final UserVO userVO = userService.findUserByUsername(username);
        return transferUser(userVO);
    }

    /**
     * 根据手机号返回用户
     *
     * @param mobile 手机号,必须唯一
     * @return
     */
    public User findUserByMobile(String mobile) {
        final UserVO userVO = userService.findUserByMobile(mobile);
        return transferUser(userVO);
    }

    /**
     * 用户信息转换
     *
     * @param userVO
     * @return User
     */
    private User transferUser(final UserVO userVO) {
        if (userVO == null) {
            return null;
        }
        final User user = new User();
        user.setUserId(userVO.getUserId());
        user.setUsername(userVO.getUsername());
        user.setPhone(userVO.getPhone());
        user.setPassword(userVO.getPassword());
        user.setDeptId(userVO.getDeptId());
        user.setDeptName(userVO.getDeptName());
        user.setEnabled(CommonConstants.STATUS_NORMAL.equals(userVO.getDelFlag()));
        user.setExpired(false);
        user.setLocked(false);
        user.setPasswordExpired(false);
        user.setCreateTime(userVO.getCreateTime());
        user.setUpdateTime(userVO.getUpdateTime());

        final List<SysRole> roleList = userVO.getRoleList();
        if (CollUtil.isNotEmpty(roleList)) {
            final List<Role> roles = new ArrayList<>(roleList.size());
            roleList.forEach(item -> roles.add(new Role(item.getRoleCode(), Collections.emptyList())));
            user.setRoles(roles);
        } else {
            user.setRoles(Collections.singletonList(defaultRole()));
        }
        return user;
    }

    private Role defaultRole() {
        return new Role(CommonConstants.ROLE_DEFAULT,
                Collections.singletonList(new Operation(CommonConstants.OP_DEFAULT)));
    }
}
