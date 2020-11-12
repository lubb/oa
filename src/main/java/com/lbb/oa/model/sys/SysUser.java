package com.lbb.oa.model.sys;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="sys_user")
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String phoneNumber;

    private Integer status;

    private Integer sex;

    private Integer type;

    private String password;

    private Date birth;

    private Long departmentId;

    private String avatar;

    private Date createTime;

    private Date modifiedTime;

}
