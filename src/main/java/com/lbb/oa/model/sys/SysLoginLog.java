package com.lbb.oa.model.sys;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "sys_login_log")
public class SysLoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private Date loginTime;

    private String location;

    private String ip;

    private String userSystem;

    private String userBrowser;

}
