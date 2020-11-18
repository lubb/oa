package com.lbb.oa.util;

import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.model.sys.SysUser;
import com.lbb.oa.pojo.sys.SecuritySysUser;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SerurityUserUtil {

    /**
     * 将sysuser转SecuritySysUser
     * @param sysUser
     * @param menus
     * @return
     */
    public static SecuritySysUser convetToSecuritySysUser(SysUser sysUser,  List<SysMenu> menus){
        SecuritySysUser securitySysUser = new SecuritySysUser();
        //菜单权限
        Set<String> urls=new HashSet<>();
        //按钮权限
        Set<String> perms=new HashSet<>();
        if(!CollectionUtils.isEmpty(menus)){
            for (SysMenu menu : menus) {
                String url = menu.getUrl();
                String per = menu.getPerms();
                if(menu.getType()==0&& !StringUtils.isEmpty(url)){
                    urls.add(menu.getUrl());
                }
                if(menu.getType()==1&&!StringUtils.isEmpty(per)){
                    perms.add(menu.getPerms());
                }
            }
        }
        securitySysUser.setId(sysUser.getId());
        securitySysUser.setUsername(sysUser.getUsername());
        securitySysUser.setPassword(sysUser.getPassword());
        securitySysUser.setAvatar(sysUser.getAvatar());
        securitySysUser.setNickname(sysUser.getNickname());
        securitySysUser.setType(sysUser.getType());
        securitySysUser.setPermissions(perms);
        return securitySysUser;
    }
}
