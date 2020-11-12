package com.lbb.oa.service.sys.impl;

import com.lbb.oa.mapper.sys.RoleMapper;
import com.lbb.oa.mapper.sys.UserRoleMapper;
import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.model.sys.SysUserRole;
import com.lbb.oa.service.sys.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 通过用户id获取用户的角色
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> getRoleByUserId(Long userId) {
        List<SysRole> roles=new ArrayList<>();
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        List<SysUserRole> userRoleList = userRoleMapper.select(sysUserRole);
        List<Long> rids=new ArrayList<>();
        if(!CollectionUtils.isEmpty(userRoleList)){
            for (SysUserRole userRole : userRoleList) {
                rids.add(userRole.getRoleId());
            }
            if(!CollectionUtils.isEmpty(rids)){
                for (Long rid : rids) {
                    SysRole role = roleMapper.selectByPrimaryKey(rid);
                    if(role!=null){
                        roles.add(role);
                    }
                }
            }
        }
        return roles;
    }
}
