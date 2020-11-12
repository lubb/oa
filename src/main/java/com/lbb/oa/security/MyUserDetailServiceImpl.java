package com.lbb.oa.security;

import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.model.sys.SysUser;
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

        SysUser sysUser = userService.selectByUsername(username);

        if(sysUser == null){
            throw new UsernameNotFoundException(username+"用户不存在");
        }
        //获取用户的角色
        List<SysRole> roles = roleService.getRoleByUserId(sysUser.getId());
        Set<String> sysRoles=new HashSet<>();
        if(roles!= null && roles.size()>0){
            for (SysRole sysRole : roles) {
                sysRoles.add(sysRole.getRoleCode());
            }
        }
        return new User(username, sysUser.getPassword(),true, true,true,true, AuthorityUtils.createAuthorityList(sysRoles.toArray(new String[sysRoles.size()])));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
