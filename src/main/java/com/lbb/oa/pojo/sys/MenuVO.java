package com.lbb.oa.pojo.sys;

import lombok.Data;

import java.util.Date;

/**
 * @Author lubingbing
 * @Date 2020/3/10 11:52
 * @Version 1.0
 **/
@Data
public class MenuVO {

    private Long id;

    private Long parentId;

    private String menuName;

    private String url;

    private String icon;

    private Integer type;

    private Long orderNum;

    private Date createTime;

    private Date modifiedTime;

    private boolean disabled;

    private Integer open;

    private String perms;
}
