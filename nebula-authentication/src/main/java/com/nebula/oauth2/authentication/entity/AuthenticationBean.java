package com.nebula.oauth2.authentication.entity;

import lombok.Data;

/**
 * 用户认证的Bean
 *
 * @author feifeixia
 * 2019/7/3 14:11
 */
@Data
public class AuthenticationBean {

    private String username;

    private String password;
}
