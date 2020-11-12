package com.lbb.oa.service.sys;

import com.lbb.oa.model.sys.SysUser;

public interface UserService {

    /**
     * 根据用户ID获取用户信息
     * @param id
     * @return
     */
    public SysUser getSysUserById(Long id);

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    public SysUser selectByUsername(String username);
}
