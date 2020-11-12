package com.lbb.oa.service.sys;

import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.model.sys.SysUserRole;

import java.util.List;

public interface RoleService {

    /**
     * 通过userId获取用户的角色
     * @param userId
     * @return
     */
    public List<SysRole> getRoleByUserId(Long userId);
}
