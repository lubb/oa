package com.lbb.oa.controller.sys;

import com.lbb.oa.util.GlobalConfig;
import com.lbb.oa.util.ResponseBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录页面控制器
 */
@RestController
public class LoginController {

    /**
     * 此处为未登录的处理，提示前端进行跳转到登录页面
     * @return
     */
    @GetMapping("/login")
    public ResponseBean login() {
        return ResponseBean.formatData(
                GlobalConfig.ResponseCode.UNLOGIN.getCode(),
                GlobalConfig.ResponseCode.UNLOGIN.getDesc(),
                null
        );
    }

}
