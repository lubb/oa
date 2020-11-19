package com.lbb.oa.pojo.sys;

import lombok.Data;

import java.util.Date;

/**
 * @Author lubingbing
 * @Date 2020/3/9 16:22
 * @Version 1.0
 **/
@Data
public class RoleVO {

    private Long id;

    private String roleName;

    private String remark;

    private Date createTime;

    private Date modifiedTime;

    private Boolean status;
}
