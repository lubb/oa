package com.lbb.oa.pojo.sys;

import lombok.Data;

import java.util.Date;

/**
 * @Author lubingbign
 * @Date 2020/3/7 19:16
 * @Version 1.0
 **/
@Data
public class UserVO {

    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String phoneNumber;

    private Boolean status;

    private Date createTime;

    private Integer sex;

    private Date birth;

    private String password;

    private String departmentName;

    private Long departmentId;

}
