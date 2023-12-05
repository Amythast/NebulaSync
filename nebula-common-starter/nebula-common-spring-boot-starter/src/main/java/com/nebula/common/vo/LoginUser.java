package com.nebula.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 当前登录用户对象
 *
 * @author feifeixia
 * 2019/1/30 15:32
 */
@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 3752922246421583643L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 角色列表
     */
    private List<SysRole> roleList = Collections.emptyList();
}
