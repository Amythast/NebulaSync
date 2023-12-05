package com.nebula.rbac.admin.controller;


import com.nebula.common.constants.CommonConstant;
import com.nebula.common.constants.RoleConst;
import com.nebula.common.utils.PageQuery;
import com.nebula.common.web.BaseController;
import com.nebula.common.web.Response;
import com.nebula.common.web.annotation.RequireRole;
import com.nebula.rbac.admin.model.entity.SysDict;
import com.nebula.rbac.admin.service.SysDictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author feifeixia
 * @since 2017-11-19
 */
@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {
    @Autowired
    private SysDictService sysDictService;

    /**
     * 通过ID查询字典信息
     *
     * @param id ID
     * @return 字典信息
     */
    @GetMapping("/{id}")
    @RequireRole(RoleConst.ADMIN)
    public SysDict dict(@PathVariable Integer id) {
        return sysDictService.getById(id);
    }

    /**
     * 分页查询字典信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @GetMapping("/dictPage")
    @RequireRole(RoleConst.ADMIN)
    public Page dictPage(@RequestParam Map<String, Object> params) {
        final QueryWrapper<SysDict> query = new QueryWrapper<>();
        query.eq(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        final String typeKey = "type";
        if (params.containsKey(typeKey) && !ObjectUtils.isEmpty(params.get(typeKey))) {
            query.like(typeKey, params.get(typeKey));
        }
        return (Page) sysDictService.page(new PageQuery<>(params), query);
    }

    /**
     * 通过字典类型查找字典
     *
     * @param type 类型
     * @return 同类型字典
     */
    @GetMapping("/type/{type}")
    @RequireRole(RoleConst.ADMIN)
    public List<SysDict> findDictByType(@PathVariable String type) {
        SysDict condition = new SysDict();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        condition.setType(type);
        return sysDictService.list(new QueryWrapper<>(condition));
    }

    /**
     * 添加字典
     *
     * @param sysDict 字典信息
     * @return success、false
     */
    @PostMapping
    @RequireRole(RoleConst.ADMIN)
    public Response dict(@RequestBody SysDict sysDict) {
        return Response.success(sysDictService.save(sysDict));
    }

    /**
     * 删除字典，并且清除字典缓存
     *
     * @param id   ID
     * @param type 类型
     * @return R
     */
    @DeleteMapping("/{id}/{type}")
    @RequireRole(RoleConst.ADMIN)
    public Response deleteDict(@PathVariable Integer id, @PathVariable String type) {
        return Response.success(sysDictService.removeById(id));
    }

    /**
     * 修改字典
     *
     * @param sysDict 字典信息
     * @return success/false
     */
    @PutMapping
    @RequireRole(RoleConst.ADMIN)
    public Response editDict(@RequestBody SysDict sysDict) {
        return Response.success(sysDictService.updateById(sysDict));
    }
}
