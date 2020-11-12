package com.lbb.oa.service.sys.impl;

import com.lbb.oa.mapper.sys.MenuMapper;
import com.lbb.oa.mapper.sys.RoleMapper;
import com.lbb.oa.mapper.sys.RoleMenuMapper;
import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.model.sys.SysRoleMenu;
import com.lbb.oa.service.sys.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 通过角色获取用户的菜单
     * @param roles
     * @return
     */
    public List<SysMenu> findMenuByRoles(List<SysRole> roles){
        List<SysMenu> menus=new ArrayList<>();
        if(!CollectionUtils.isEmpty(roles)){
            Set<Long> menuIds=new HashSet<>();//存放用户的菜单id
            List<SysRoleMenu> roleMenus;
            for (SysRole role : roles) {
                //根据角色ID查询权限ID
                Example o = new Example(SysRoleMenu.class);
                o.createCriteria().andEqualTo("roleId",role.getId());
                roleMenus= roleMenuMapper.selectByExample(o);
                if(!CollectionUtils.isEmpty(roleMenus)){
                    for (SysRoleMenu roleMenu : roleMenus) {
                        menuIds.add(roleMenu.getMenuId());
                    }
                }
            }
            if(!CollectionUtils.isEmpty(menuIds)){
                for (Long menuId : menuIds) {
                    //该用户所有的菜单
                    SysMenu menu = menuMapper.selectByPrimaryKey(menuId);
                    if(menu!=null){
                        menus.add(menu);
                    }
                }
            }
        }
        return menus;
    }

    /**
     * 获取用户的菜单 包含角色
     * @return
     */
    @Override
    public List<SysMenu> getAllMenuswithRole() {
        List<SysMenu> menus = menuMapper.selectAll();
        for (SysMenu sysMenu : menus){
            List<SysRole> roles = roleMapper.getSysRoleByMenuId(sysMenu.getId());
            sysMenu.setRoles(roles);
        }
        return menus;
    }
}
