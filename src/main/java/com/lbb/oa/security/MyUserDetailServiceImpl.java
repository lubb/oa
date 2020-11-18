package com.lbb.oa.security;

import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.model.sys.SysUser;
import com.lbb.oa.service.sys.MenuService;
import com.lbb.oa.service.sys.RoleService;
import com.lbb.oa.service.sys.UserService;
import com.lbb.oa.util.SerurityUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

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
        SysUser sysUser = userService.selectByUsername(username);
        if(sysUser == null){
            throw new UsernameNotFoundException(username+"用户不存在");
        }
        //获取用户的角色
        List<SysRole> roles = roleService.getRoleByUserId(sysUser.getId());
        //获取用户的菜单和按钮权限
        List<SysMenu> menus = menuService.findMenuByRoles(roles);
        return SerurityUserUtil.convetToSecuritySysUser(sysUser, menus);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
