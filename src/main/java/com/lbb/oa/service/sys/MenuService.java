package com.lbb.oa.service.sys;

import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.model.sys.SysRole;

import java.util.List;

public interface MenuService {

    /**
     * 获取角色对应的菜单
     * @param roles
     * @return
     */
    public List<SysMenu> findMenuByRoles(List<SysRole> roles);

}
