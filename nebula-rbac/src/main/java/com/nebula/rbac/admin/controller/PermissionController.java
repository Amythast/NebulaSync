package com.nebula.rbac.admin.controller;

import com.nebula.common.constants.CommonConstant;
import com.nebula.common.constants.RoleConst;
import com.nebula.common.exception.InvalidParamException;
import com.nebula.common.utils.PageQuery;
import com.nebula.common.web.BaseController;
import com.nebula.common.web.Response;
import com.nebula.common.web.annotation.RequireRole;
import com.nebula.rbac.admin.model.entity.SysPermission;
import com.nebula.rbac.admin.service.SysPermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
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
import java.util.Map;

/**
 * 权限管理
 *
 * @author feifeixia
 * 2019/2/19 16:25
 */
@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController {

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 分页查询
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @RequireRole(RoleConst.ADMIN)
    public Response page(@RequestParam Map<String, Object> params) {
        final QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        final String codeKey = "code";
        if (params.containsKey(codeKey) && !ObjectUtils.isEmpty(params.get(codeKey))) {
            queryWrapper.like(codeKey, params.get(codeKey));
        }
        return Response.success(permissionService.page(new PageQuery<>(params), queryWrapper));
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @GetMapping("/{id}")
    @RequireRole(RoleConst.ADMIN)
    public Response get(@PathVariable Integer id) {
        return Response.success(permissionService.getById(id));
    }

    /**
     * 根据ID删除
     *
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    @RequireRole(RoleConst.ADMIN)
    public Response delete(@PathVariable Integer id) {
        return Response.success(permissionService.deleteById(id));
    }

    /**
     * 新增权限
     *
     * @param permission 权限实体
     * @param result     错误信息
     */
    @PostMapping
    @RequireRole(RoleConst.ADMIN)
    public Response add(@Valid @RequestBody SysPermission permission, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result.getAllErrors().get(0).getDefaultMessage());
        }
        return Response.success(permissionService.addPermission(permission));
    }

    /**
     * 编辑权限
     *
     * @param permission 权限实体
     * @param result     错误信息
     */
    @PutMapping
    @RequireRole(RoleConst.ADMIN)
    public Response update(@Valid @RequestBody SysPermission permission, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamException(result.getAllErrors().get(0).getDefaultMessage());
        }
        return Response.success(permissionService.updatePermission(permission));
    }
}
