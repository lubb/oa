package com.lbb.oa.model.sys;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name="sys_role_menu")
public class SysRoleMenu {

    private Long roleId;

    private Long menuId;

}
