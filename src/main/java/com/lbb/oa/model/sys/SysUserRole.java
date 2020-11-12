package com.lbb.oa.model.sys;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name="sys_user_role")
public class SysUserRole {

    private Long userId;

    private Long roleId;
}
