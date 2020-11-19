package com.lbb.oa.pojo.sys;

import lombok.Data;

import java.util.Date;

/**
 * @Author lubingbing
 * @Date 2020/3/9 11:06
 * @Version 1.0
 **/
@Data
public class UserEditVO {
    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String phoneNumber;

    private Integer sex;

    private Date birth;

    private Long departmentId;

}
