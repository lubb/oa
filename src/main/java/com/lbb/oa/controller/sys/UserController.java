package com.lbb.oa.controller.sys;

import com.lbb.oa.model.sys.SysUser;
import com.lbb.oa.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public SysUser getUser(){
        return userService.getSysUserById(189L);
    }
}
