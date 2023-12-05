package com.nebula.oauth2.authentication.entity;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户Vo
 *
 * @author feifeixia
 * 2019/7/3 9:40
 */
@Data
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 8741046663436494432L;

    /**
     * 主键ID
     */
    private Integer userId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 部门ID
     */
    private Integer deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 是否过期
     */
    private Boolean expired;

    /**
     * 是否锁定
     */
    private Boolean locked;

    /**
     * 是否可用
     */
    private Boolean enabled;

    /**
     * 密码是否过期
     */
    private Boolean passwordExpired;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 角色列表
     */
    private List<Role> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollUtil.isEmpty(roles)) {
            return Collections.emptyList();
        }
        final List<GrantedAuthority> authorities = new ArrayList<>(AuthorityUtils.createAuthorityList(
                roles.stream().map(Role::getAuthority).collect(Collectors.joining(","))));
        roles.forEach(role -> {
            if (CollUtil.isNotEmpty(role.getOperations())) {
                authorities.addAll(AuthorityUtils.createAuthorityList(
                        role.getOperations().stream().map(Operation::getAuthority).collect(Collectors.joining(","))));
            }
        });
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !passwordExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
