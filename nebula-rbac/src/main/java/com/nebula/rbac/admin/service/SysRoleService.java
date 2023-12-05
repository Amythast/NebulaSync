package com.nebula.rbac.admin.service;

import com.nebula.common.utils.PageQuery;
import com.nebula.rbac.admin.model.dto.RoleDTO;
import com.nebula.rbac.admin.model.entity.SysRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author feifeixia
 * @since 2017-10-29
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 添加角色
     *
     * @param roleDto 角色信息
     * @return 成功、失败
     */
    Boolean insertRole(RoleDTO roleDto);

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param pageQuery 查询条件
     * @param roleName 角色名称
     * @return
     */
    IPage<RoleDTO> selectPageVo(PageQuery pageQuery, String roleName);

    /**
     * 分页查角色列表
     *
     * @param objectPageQuery         查询条件
     * @param objectEntityWrapper wapper
     * @return page
     */
    Page selectwithDeptPage(PageQuery objectPageQuery, QueryWrapper objectEntityWrapper);

    /**
     * 更新角色
     * @param roleDto 含有部门信息
     * @return 成功、失败
     */
    Boolean updateRoleById(RoleDTO roleDto);

    /**
     * 通过部门ID查询角色列表
     * @param deptId 部门ID
     * @return 角色列表
     */
    List<SysRole> selectListByDeptId(Integer deptId);
}
