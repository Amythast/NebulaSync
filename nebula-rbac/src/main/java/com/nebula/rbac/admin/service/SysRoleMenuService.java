package com.nebula.rbac.admin.service;


import com.nebula.rbac.admin.model.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author feifeixia
 * @since 2017-10-29
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 更新角色菜单
     *
     *
     * @param roleId  角色
     * @param menuIds 菜单列表
     * @return
     */
    Boolean insertRoleMenus(Integer roleId, Collection<Integer> menuIds);
}
