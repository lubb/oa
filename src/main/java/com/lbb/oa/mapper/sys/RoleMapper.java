package com.lbb.oa.mapper.sys;


import com.lbb.oa.model.sys.SysRole;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author lubingbing
 * @Date 2020/3/7 15:54
 * @Version 1.0
 **/
public interface RoleMapper extends Mapper<SysRole> {

    List<SysRole> getSysRoleByMenuId(@Param("menuId") Long menuId);
}
