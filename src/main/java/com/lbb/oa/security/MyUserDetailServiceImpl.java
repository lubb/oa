package com.lbb.oa.security;

import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.model.sys.SysUser;
import com.lbb.oa.pojo.SecuritySysUser;
import com.lbb.oa.service.sys.MenuService;
import com.lbb.oa.service.sys.RoleService;
import com.lbb.oa.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MyUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecuritySysUser securitySysUser = new SecuritySysUser();
        SysUser sysUser = userService.selectByUsername(username);
        if(sysUser == null){
            throw new UsernameNotFoundException(username+"用户不存在");
        }
        //获取用户的角色
        List<SysRole> roles = roleService.getRoleByUserId(sysUser.getId());
        //获取用户的菜单和按钮权限
        List<SysMenu> menus = menuService.findMenuByRoles(roles);
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
        securitySysUser.setRoles(roles);
        securitySysUser.setUrls(urls);
        securitySysUser.setPermissions(perms);
        return securitySysUser;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
