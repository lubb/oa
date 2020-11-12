package com.lbb.oa.model.sys;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "sys_log")
public class SysLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private Long time;

    private String ip;

    private Date createTime;

    private String location;

    private String operation;

    private String method;

    private String params;
}
