package com.nebula.rbac.admin.service.impl;

import com.nebula.rbac.admin.mapper.SysRoleMenuMapper;
import com.nebula.rbac.admin.model.entity.SysRoleMenu;
import com.nebula.rbac.admin.service.SysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author feifeixia
 * @since 2017-10-29
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
    @Override
    public Boolean insertRoleMenus(Integer roleId, Collection<Integer> menuIds) {
        this.removeById(roleId);

        List<SysRoleMenu> roleMenuList = new ArrayList<>();
        for (Integer menuId : menuIds) {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuList.add(roleMenu);
        }
        return this.saveBatch(roleMenuList);
    }
}
