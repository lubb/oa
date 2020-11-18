package com.lbb.oa.controller.sys;

import com.lbb.oa.model.sys.SysUser;
import com.lbb.oa.pojo.sys.MenuNodeVO;
import com.lbb.oa.service.sys.UserService;
import com.lbb.oa.util.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/findMenu")
    public ResponseBean findMenu(){
        List<MenuNodeVO> menuTreeVOS = userService.findMenu();
        return ResponseBean.success(menuTreeVOS);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('user:add')")
    public ResponseBean add(){
        SysUser sysUser = userService.getSysUserById(189L);
        return ResponseBean.success(sysUser);
    }
}
