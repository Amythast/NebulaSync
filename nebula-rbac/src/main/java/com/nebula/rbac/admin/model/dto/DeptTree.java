package com.nebula.rbac.admin.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author feifeixia
 * @date 2018/1/20
 * 部门树
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeptTree extends TreeNode {

    private String name;

    private Integer orderNum;
}
