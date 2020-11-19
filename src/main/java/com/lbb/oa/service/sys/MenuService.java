package com.lbb.oa.service.sys;

import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.pojo.sys.MenuNodeVO;
import com.lbb.oa.pojo.sys.MenuVO;

import java.util.List;

public interface MenuService {

    /**
     * 获取角色对应的菜单
     * @param roles
     * @return
     */
    public List<SysMenu> findMenuByRoles(List<SysRole> roles);

    /**
     * 删除节点
     * @param id
     */
    void delete(Long id);

    /**
     * 所有展开菜单的ID
     * @return
     */
    List<Long> findOpenIds();

    /**
     * 获取所有菜单
     * @return
     */
    List<SysMenu> findAll();

    /**
     * 获取菜单树
     * @return
     */
    List<MenuNodeVO> findMenuTree();

    /**
     * 添加菜单
     * @param menuVO
     * @return
     */
    SysMenu add(MenuVO menuVO);

    /**
     * 菜单详情
     * @param id
     * @return
     */
    MenuVO edit(Long id);

    /**
     * 更新节点
     * @param id
     */
    void update(Long id, MenuVO menuVO);

}
