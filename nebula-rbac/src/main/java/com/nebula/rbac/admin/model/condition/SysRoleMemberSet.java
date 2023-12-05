package com.nebula.rbac.admin.model.condition;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 角色成员设置
 *
 * @author feifeixia
 * 2019/2/19 17:20
 */
@Data
public class SysRoleMemberSet {

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    private Integer roleId;

    /**
     * 成员id
     */
    private Set<Integer> userIds;
}
