package com.lbb.oa.service.sys.impl;

import com.lbb.oa.mapper.sys.UserMapper;
import com.lbb.oa.model.sys.SysUser;
import com.lbb.oa.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户ID获取用户信息
     * @param id
     * @return
     */
    public SysUser getSysUserById(Long id){
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public SysUser selectByUsername(String username) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        return userMapper.selectOne(sysUser);
    }
}
