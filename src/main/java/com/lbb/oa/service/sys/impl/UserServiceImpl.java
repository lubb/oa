package com.lbb.oa.service.sys.impl;

import com.lbb.oa.mapper.sys.MenuMapper;
import com.lbb.oa.mapper.sys.UserMapper;
import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.model.sys.SysUser;
import com.lbb.oa.pojo.sys.MenuNodeVO;
import com.lbb.oa.pojo.sys.SecuritySysUser;
import com.lbb.oa.service.sys.UserService;
import com.lbb.oa.util.GlobalConfig;
import com.lbb.oa.util.MenuConverterUtil;
import com.lbb.oa.util.MenuTreeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 根据用户ID获取用户信息
     * @param id
     * @return
     */
    public SysUser getSysUserById(Long id){
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    @Override
    public SysUser selectByUsername(String username) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        return userMapper.selectOne(sysUser);
    }

    /**
     * 获取用户的菜单
     * @return
     */
    @Override
    public List<MenuNodeVO> findMenu() {
        List<SysMenu> menus=null;
        List<MenuNodeVO> menuNodeVOS=new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuritySysUser securitySysUser = (SecuritySysUser) authentication.getPrincipal();
        if(securitySysUser.getType()== GlobalConfig.UserTypeEnum.SYSTEM_ADMIN.getTypeCode()){
            //超级管理员
            menus=menuMapper.selectAll();
        }else if(securitySysUser.getType()== GlobalConfig.UserTypeEnum.SYSTEM_USER.getTypeCode()){
            //普通系统用户
            menus= securitySysUser.getMenus();
        }
        if(!CollectionUtils.isEmpty(menus)){
            menuNodeVOS= MenuConverterUtil.converterToMenuNodeVO(menus);
        }
        //构建树形菜单
        return MenuTreeBuilder.build(menuNodeVOS);
    }
}
