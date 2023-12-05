package com.nebula.rbac.admin.controller;

import com.nebula.common.constants.CommonConstant;
import com.nebula.common.constants.RoleConst;
import com.nebula.common.utils.PageQuery;
import com.nebula.common.web.BaseController;
import com.nebula.common.web.Response;
import com.nebula.common.web.annotation.RequireRole;
import com.nebula.rbac.admin.model.condition.SysRoleMemberSet;
import com.nebula.rbac.admin.model.condition.SysRoleMenuSet;
import com.nebula.rbac.admin.model.condition.SysRolePermissionSet;
import com.nebula.rbac.admin.model.dto.RoleDTO;
import com.nebula.rbac.admin.model.entity.SysRole;
import com.nebula.rbac.admin.service.SysRoleMenuService;
import com.nebula.rbac.admin.service.SysRolePermissionService;
import com.nebula.rbac.admin.service.SysRoleService;
import com.nebula.rbac.admin.service.SysUserRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author feifeixia
 * @date 2017/11/5
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    /**
     * 通过ID查询角色信息
     *
     * @param id ID
     * @return 角色信息
     */
    @GetMapping("/{id}")
    @RequireRole(RoleConst.ADMIN)
    public SysRole role(@PathVariable Integer id) {
        return sysRoleService.getById(id);
    }

    /**
     * 添加角色
     *
     * @param roleDto 角色信息
     * @return success、false
     */
    @PostMapping
    @RequireRole(RoleConst.ADMIN)
    public Response role(@RequestBody RoleDTO roleDto) {
        return Response.success(sysRoleService.insertRole(roleDto));
    }

    /**
     * 修改角色
     *
     * @param roleDto 角色信息
     * @return success/false
     */
    @PutMapping
    @RequireRole(RoleConst.ADMIN)
    public Response roleUpdate(@RequestBody RoleDTO roleDto) {
        return Response.success(sysRoleService.updateRoleById(roleDto));
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @RequireRole(RoleConst.ADMIN)
    public Response roleDel(@PathVariable Integer id) {
        SysRole sysRole = sysRoleService.getById(id);
        sysRole.setDelFlag(CommonConstant.STATUS_DEL);
        return Response.success(sysRoleService.updateById(sysRole));
    }

    /**
     * 获取角色列表
     *
     * @param deptId 部门ID
     * @return 角色列表
     */
    @GetMapping("/roleList/{deptId}")
    @RequireRole(RoleConst.ADMIN)
    public List<SysRole> roleList(@PathVariable Integer deptId) {
        return sysRoleService.selectListByDeptId(deptId);

    }

    /**
     * 分页查询角色信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @GetMapping("/rolePage")
    @RequireRole(RoleConst.ADMIN)
    public IPage<RoleDTO> rolePage(@RequestParam Map<String, Object> params) {
        return sysRoleService.selectPageVo(new PageQuery<>(params), (String) params.get("roleName"));
    }

    /**
     * 更新角色成员
     *
     * @return success、false
     */
    @PutMapping("/members/{operate}")
    @RequireRole(RoleConst.ADMIN)
    public Response roleMembersUpdate(@Valid @RequestBody SysRoleMemberSet memberSet, @PathVariable String operate) {

        final String add = "add";
        final String delete = "delete";
        Boolean fg = false;
        if (add.equals(operate)) {
            fg = sysUserRoleService.insertRoleMembers(memberSet.getRoleId(), memberSet.getUserIds());
        } else if (delete.equals(operate)) {
            fg = sysUserRoleService.deleteRoleMembers(memberSet.getRoleId(), memberSet.getUserIds());
        }

        return Response.success(fg);
    }

    /**
     * 查询已添加成员
     *
     * @param roleId
     * @return
     */
    @GetMapping("/members/added/{roleId}")
    @RequireRole(RoleConst.ADMIN)
    public Response roleMembersAdded(@PathVariable Integer roleId, @RequestParam Map<String, Object> params) {
        return Response.success(sysUserRoleService.roleMembersAdded(new PageQuery<>(params), roleId, params));
    }

    /**
     * 查询未添加成员
     *
     * @param roleId
     * @return
     */
    @GetMapping("/members/notin/{roleId}")
    @RequireRole(RoleConst.ADMIN)
    public Response roleMembersNotin(@PathVariable Integer roleId, @RequestParam Map<String, Object> params) {
        return Response.success(sysUserRoleService.roleMembersNotin(new PageQuery<>(params), roleId, params));
    }

    /**
     * 更新角色菜单
     *
     * @return success、false
     */
    @PutMapping("/roleMenuUpd")
    @RequireRole(RoleConst.ADMIN)
    public Response roleMenuUpd(@Valid @RequestBody SysRoleMenuSet menuSet) {
        return Response.success(sysRoleMenuService.insertRoleMenus(menuSet.getRoleId(), menuSet.getMenuIds()));
    }

    /**
     * 更新角色权限
     *
     * @return success、false
     */
    @PutMapping("/permissions/{operate}")
    @RequireRole(RoleConst.ADMIN)
    public Response rolePermissions(@Valid @RequestBody SysRolePermissionSet permissionSet, @PathVariable String operate) {

        final String add = "add";
        final String delete = "delete";
        Boolean fg = false;
        if (add.equals(operate)) {
            fg = sysRolePermissionService.insertRolePermissions(permissionSet.getRoleId(), permissionSet.getPermissionIds());
        } else if (delete.equals(operate)) {
            fg = sysRolePermissionService.deleteRolePermissions(permissionSet.getRoleId(), permissionSet.getPermissionIds());
        }
        return Response.success(fg);
    }

    /**
     * 查询已添加权限
     *
     * @param roleId
     * @return
     */
    @GetMapping("/permissions/added/{roleId}")
    @RequireRole(RoleConst.ADMIN)
    public Response rolePermissionsAdded(@PathVariable Integer roleId, @RequestParam Map<String, Object> params) {
        return Response.success(sysRolePermissionService.rolePermissionsAdded(new PageQuery<>(params), roleId, params));
    }

    /**
     * 查询未添加权限
     *
     * @param roleId
     * @return
     */
    @GetMapping("/permissions/notin/{roleId}")
    @RequireRole(RoleConst.ADMIN)
    public Response rolePermissionsNotin(@PathVariable Integer roleId, @RequestParam Map<String, Object> params) {
        return Response.success(sysRolePermissionService.rolePermissionsNotin(new PageQuery<>(params), roleId, params));
    }
}
