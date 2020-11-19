package com.lbb.oa.service.sys.impl;

import com.lbb.oa.exception.ServiceException;
import com.lbb.oa.mapper.sys.MenuMapper;
import com.lbb.oa.mapper.sys.RoleMenuMapper;
import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.model.sys.SysRoleMenu;
import com.lbb.oa.pojo.sys.MenuNodeVO;
import com.lbb.oa.pojo.sys.MenuVO;
import com.lbb.oa.service.sys.MenuService;
import com.lbb.oa.util.MenuConverterUtil;
import com.lbb.oa.util.MenuTreeBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

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
     * 加载菜单树（按钮和菜单）
     *
     * @return
     */
    @Override
    public List<MenuNodeVO> findMenuTree() {
        List<SysMenu> menus = menuMapper.selectAll();
        List<MenuNodeVO> menuNodeVOS = MenuConverterUtil.converterToALLMenuNodeVO(menus);
        return MenuTreeBuilder.build(menuNodeVOS);
    }

    /**
     * 添加菜单
     *
     * @param menuVO
     */
    @Override
    public SysMenu add(MenuVO menuVO) {
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(menuVO,menu);
        menu.setCreateTime(new Date());
        menu.setModifiedTime(new Date());
        menu.setAvailable(menuVO.isDisabled()?0:1);
        menuMapper.insert(menu);
        return menu;
    }

    /**
     * 删除菜单
     * @param id
     */
    @Override
    public void delete(Long id) {
        SysMenu menu = menuMapper.selectByPrimaryKey(id);
        if(menu==null){
            throw new ServiceException("要删除的菜单不存在");
        }
        menuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 编辑菜单
     * @param id
     * @return
     */
    @Override
    public MenuVO edit(Long id) {
        SysMenu menu = menuMapper.selectByPrimaryKey(id);
        if(menu==null){
            throw new ServiceException("该编辑的菜单不存在");
        }
        return MenuConverterUtil.converterToMenuVO(menu);
    }

    /**
     * 更新菜单
     * @param id
     * @param menuVO
     */
    @Override
    public void update(Long id, MenuVO menuVO) {
        SysMenu dbMenu = menuMapper.selectByPrimaryKey(id);
        if(dbMenu==null){
            throw new ServiceException("要更新的菜单不存在");
        }
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(menuVO,menu);
        menu.setId(id);
        menu.setAvailable(menuVO.isDisabled()?0:1);
        menu.setModifiedTime(new Date());
        menuMapper.updateByPrimaryKeySelective(menu);
    }

    /**
     * 获取展开项
     *
     * @return
     */
    @Override
    public List<Long> findOpenIds() {
        List<Long> ids=new ArrayList<>();
        List<SysMenu> menus = menuMapper.selectAll();
        if(!CollectionUtils.isEmpty(menus)){
            for (SysMenu menu : menus) {
                if(menu.getOpen()==1){
                    ids.add(menu.getId());
                }
            }
        }
        return ids;
    }

    /**
     * 获取所有菜单
     * @return
     */
    @Override
    public List<SysMenu> findAll() {
        return menuMapper.selectAll();
    }
}
