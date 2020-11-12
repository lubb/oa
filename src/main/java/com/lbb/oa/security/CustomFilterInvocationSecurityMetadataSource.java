package com.lbb.oa.security;

import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.service.sys.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private MenuService menuService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<SysMenu> menus = menuService.getAllMenuswithRole();
        for (SysMenu menu : menus) {
            if(menu.getPath()!=null){
                if (antPathMatcher.match(menu.getPath(), requestUrl)) {
                    List<SysRole> roles = menu.getRoles();
                    String[] str = new String[roles.size()];
                    for (int i = 0; i < roles.size(); i++) {
                        str[i] = roles.get(i).getRoleCode();
                    }
                    return SecurityConfig.createList(str);
                }
            }

        }
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    public static void main(String[] args) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        System.out.println(antPathMatcher.match("/user/add", "/user/add"));
    }
}
