package com.nebula.rbac.admin.controller;

import com.nebula.common.constants.CommonConstant;
import com.nebula.common.constants.RoleConst;
import com.nebula.common.constants.SecurityConstants;
import com.nebula.common.vo.MenuVO;
import com.nebula.common.web.BaseController;
import com.nebula.common.web.Response;
import com.nebula.common.web.annotation.RequireRole;
import com.nebula.rbac.admin.common.util.TreeUtil;
import com.nebula.rbac.admin.model.dto.MenuTree;
import com.nebula.rbac.admin.model.entity.SysMenu;
import com.nebula.rbac.admin.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author feifeixia
 * @date 2017/10/31
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {
    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 通过角色名称查询用户菜单
     *
     * @param role 角色名称
     * @return 菜单列表
     */
    @GetMapping("/findMenuByRole/{role}")
    @RequireRole(RoleConst.ADMIN)
    public List<MenuVO> findMenuByRole(@PathVariable String role) {
        return sysMenuService.findMenuByRoleName(role);
    }

    /**
     * 返回当前用户的树形菜单集合
     *
     * @return 当前用户的树形菜单
     */
    @GetMapping(value = "/userMenu")
    public List<MenuTree> userMenu(@RequestHeader(name = SecurityConstants.USER_ROLE_HEADER) String roles) {
        if (StringUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }
        // 获取符合条件得菜单
        Set<MenuVO> all = new HashSet<>();
        Arrays.stream(roles.split(",")).forEach(role -> all.addAll(sysMenuService.findMenuByRoleName(role)));
        List<MenuTree> menuTreeList = new ArrayList<>();
        all.forEach(menuVo -> {
            menuTreeList.add(new MenuTree(menuVo));
        });
        menuTreeList.sort(Comparator.comparing(MenuTree::getSort));
        return TreeUtil.bulid(menuTreeList, -1);
    }

    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/allTree")
    @RequireRole(RoleConst.ADMIN)
    public List<MenuTree> getTree() {
        SysMenu condition = new SysMenu();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        return TreeUtil.bulidTree(sysMenuService.list(new QueryWrapper<>(condition)), -1);
    }

    /**
     * 返回角色的菜单集合
     *
     * @param roleName 角色名称
     * @return 属性集合
     */
    @GetMapping("/roleTree/{roleName}")
    @RequireRole(RoleConst.ADMIN)
    public List<Integer> roleTree(@PathVariable String roleName) {
        List<MenuVO> menus = sysMenuService.findMenuByRoleName(roleName);
        List<Integer> menuList = new ArrayList<>();
        for (MenuVO menuVo : menus) {
            menuList.add(menuVo.getId());
        }
        return menuList;
    }

    /**
     * 通过ID查询菜单的详细信息
     *
     * @param id 菜单ID
     * @return 菜单详细信息
     */
    @GetMapping("/{id}")
    @RequireRole(RoleConst.ADMIN)
    public SysMenu menu(@PathVariable Integer id) {
        return sysMenuService.getById(id);
    }

    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return success/false
     */
    @PostMapping
    @RequireRole(RoleConst.ADMIN)
    public Response menu(@RequestBody SysMenu sysMenu) {
        return Response.success(sysMenuService.save(sysMenu));
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    @RequireRole(RoleConst.ADMIN)
    public Response menuDel(@PathVariable Integer id) {
        return Response.success(sysMenuService.deleteMenu(id));
    }

    /**
     * 更新菜单
     *
     * @param sysMenu
     * @return
     */
    @PutMapping
    @RequireRole(RoleConst.ADMIN)
    public Response menuUpdate(@RequestBody SysMenu sysMenu) {
        return Response.success(sysMenuService.updateMenuById(sysMenu));
    }

}
