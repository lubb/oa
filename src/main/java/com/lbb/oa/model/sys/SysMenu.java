package com.lbb.oa.model.sys;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@Table(name="sys_menu")
public class SysMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;

    private String menuName;

    //此url为前端compent的地址
    private String url;

    private String icon;

    private Integer open;

    private Integer type;

    private Long orderNum;

    private Integer available;

    //此地址为后段请求的地址
    private String path;

    private Date createTime;

    private Date modifiedTime;

    private List<SysRole> roles;
}
