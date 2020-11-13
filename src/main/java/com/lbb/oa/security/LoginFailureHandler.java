package com.lbb.oa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbb.oa.util.GlobalConfig;
import com.lbb.oa.util.ResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("loginFailureHandler")
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException exception) throws IOException, ServletException {
        logger.info("登录失败");
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String msg = "";
        if (exception instanceof LockedException) {
            msg = "账户被锁定，请联系管理员!";
        } else if (exception instanceof CredentialsExpiredException) {
            msg ="密码过期，请联系管理员!";
        } else if (exception instanceof AccountExpiredException) {
            msg ="账户过期，请联系管理员!";
        } else if (exception instanceof DisabledException) {
            msg ="账户被禁用，请联系管理员!";
        } else if (exception instanceof BadCredentialsException) {
            msg ="用户名或者密码输入错误，请重新输入!";
        }
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(
                ResponseBean.formatData(GlobalConfig.ResponseCode.ERROR.getCode(),
                        GlobalConfig.ResponseCode.ERROR.getDesc(),
                        "登录失败:"+ msg
                )));
    }
}



